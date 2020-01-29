
let debug = true;
let host = "http://localhost:8080";


class Navigator{


    contentElement;

    navigationItems = {};

    currentPage = null;

    authentication = null;

    parameterManager = new ParameterManager(this);

    constructor(page = "index") {
        this.contentElement = $("#content");

        let authentication = window.localStorage.getItem('authentication');

        if(authentication !== null){
            this.authentication = authentication;
        }



        this.updateNavigation(function(navigator){

            let convertedHash = navigator.convertHash();
            if(typeof convertedHash.page !== "undefined"){
                page = convertedHash.page;
            }
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

            let show = true;
            if(typeof obj.data('show') !== "undefined"){
                show = obj.data('show') === true;
            }

            if(!show){
                obj.hide();
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
                show: show,
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

        this.currentPage = page;

        $.ajax({
            url: navigationItem.url + (debug ? '?_=' + new Date().getTime() : ''),
            method: "get",
            success: function(data){
                contentElement.html(data);
                for (let name in navigationItems){
                    if(!navigationItems.hasOwnProperty(name)) continue;
                    navigationItems[name].element.removeClass('active');
                    if(!navigationItems[name].show){
                        navigationItems[name].element.hide();
                    }
                }
                navigationItem.element.addClass('active');
                navigationItem.element.show();


                navigator.updateHash();

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

    updateHash(){

        let hash = this.currentPage;
        let params = this.parameterManager.all();
        for(let key in params){
            if(!params.hasOwnProperty(key)) continue;
            hash += "/" + key + "/" + params[key];
        }
        window.location.hash = hash;
    }
    convertHash(){
        let data = {};
        let url = window.location.href;

        if(url.indexOf("#") !== -1){
            let hash = url.substring(url.indexOf("#")+1);
            let splittedHash = hash.split("/");

            data.page = splittedHash.shift();
            data.params = {};

            for(let i=0;i<splittedHash.length-1;i+=2){
                data.params[splittedHash[i]] = splittedHash[i+1];
            }
        }
        return data;
    }

}
let navigator;
$(function(){
    navigator = new Navigator();
    // console.log('hi');
});
