
let debug = true;


class Navigator{


    contentElement;

    navigationItems = {};

    authentication = null;

    constructor(page = "index") {
        this.contentElement = $("#content");

        let authentication = window.localStorage.getItem('authentication');

        if(authentication !== null){
            this.authentication = authentication;
            // TODO: check authentication
        }

        var url = window.location.href;

        if(url.indexOf("#") !== -1){
            var hash = url.substring(url.indexOf("#")+1);
            if(typeof this.navigationItems[hash] !== "undefined"){
                page = hash;
            }
        }


        this.updateNavigation(function(navigator){
            navigator.load(page);
        });

    }

    updateNavigation(callback){


        let authentication = this.authentication;


        let navigationUrl = "anonymous";
        if(authentication){
            // TODO: check authentication
            navigationUrl = "logged-in";
        }
        let navigator = this;
        $.ajax({
            url: "navigation/" + navigationUrl + ".html" + (debug ? '?_=' + new Date().getTime() : ''),
            method: "get",
            success: function (data) {
                $("#navigation").html(data);
                navigator.navigationItems = navigator.updateNavElements(authentication !== null);
                if(typeof callback !== "undefined") callback(navigator);
            }
        });

    }

    updateNavElements(authorised){


        let navigationItems = {};



        $(".navigation .nav-item").each(function(){


            // Set obj so we don't need to call jQuery selector every time
            let obj = $(this);

            // Url of the link
            let url = "";

            // Type of the link
            let type = typeof obj.data('type') === "undefined" ? "html" : obj.data('type');

            // Name of the link
            let name = obj.data('name');

            let js = "";
            if(typeof obj.data('with-js') !== "undefined" && obj.data('with-js') === true){
                js = "js/page/"+name+".js";
            }


            // LoggedIn rules
            // 0: for not logged in users
            // 1: for logged in users
            // 2: for both
            let loggedIn = 2;
            if(typeof obj.data('logged-in') !== "undefined"){
                loggedIn = obj.data('logged-in') === true;
            }


            // Set the url
            switch (type) {
                case "html":
                    url = "html/" + name + ".html";

                    // Set onclick handler
                    obj.find("a").click(function (e) {
                        e.preventDefault();

                        navigator.load($(this).parent().data('name'));

                        return false;
                    });

                    break;
                case "js":
                    url = null;

                    // Set onclick handler
                    obj.find("a").click(function (e) {
                        e.preventDefault();

                        navigator.loadJs(js);

                        return false;
                    });

                    break;
                case "extern":
                    url = obj.find('a').attr('href');
                    break;
            }

            // Add navigation item
            navigationItems[obj.data('name')] = {
                element: obj,
                type: type,
                url: url,
                js: js,
                loggedIn: loggedIn
            };


            if(authorised !== null && loggedIn === 0){
                obj.hide();
            }
            if(authorised === null && loggedIn === 1){
                obj.hide();
            }


        });
        // return navigationItems
        return navigationItems;
    }



    load(page){
        if(debug) console.log("Load page " + page);
        if(typeof this.navigationItems[page] === "undefined"){
            alert('Page don\'t exists');
            return;
        }

        let contentElement = this.contentElement;
        let navigationItems = this.navigationItems;
        let navigationItem = navigationItems[page];

        if(navigationItem.type === "extern"){
            return;
        }

        $.ajax({
            url: navigationItem.url + (debug ? '?_=' + new Date().getTime() : ''),
            method: "get",
            success: function(data){
                contentElement.html(data);
                for (let i=0; i<navigationItems.length; i++){
                    navigationItems[i].element.removeClass('active');
                }
                navigationItem.element.addClass('active');

                window.location.hash = page;

                if(navigationItem.js !== ""){
                    navigator.loadJs(navigationItem.js);
                }
            },
            error: function(){
                alert('Couldn\'t load the page ' + page);
            }
        });


    }

    loadJs(js){
        $.ajax({
            url: js + (debug ? '?_=' + new Date().getTime() : ''),
            method: "get",
            success: function (data) {
                // Don't need
            }
        });
    }

    setAuthentication(authentication){
        this.authentication = authentication;
        if(authentication == null){
            window.localStorage.removeItem('authentication');
        }else{
            window.localStorage.setItem('authentication', this.authentication);
        }

        this.updateNavigation();
    }
}
let navigator;
$(function(){
    navigator = new Navigator();
    // console.log('hi');
});
