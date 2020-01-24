
let debug = true;


class Navigator{


    contentElement;

    navigationItems = {};



    constructor(page = "index") {
        this.contentElement = $("#content");


        this.updateNavigation();


        var url = window.location.href;

        if(url.indexOf("#") !== -1){
            var hash = url.substring(url.indexOf("#")+1);
            if(typeof this.navigationItems[hash] !== "undefined"){
                page = hash;
            }
        }
        this.load(page);
    }

    updateNavigation(){

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
                case "extern":
                    url = obj.find('a').attr('href');
                    break;
            }

            // Add navigation item
            navigationItems[obj.data('name')] = {
                element: obj,
                type: type,
                url: url,
                js: js
            };
        });

        // Update the navigation items
        this.navigationItems = navigationItems;

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

                    $.ajax({
                        url: navigationItem.js + (debug ? '?_=' + new Date().getTime() : ''),
                        method: "get",
                        success: function (data) {
                            // contentElement.append("<script>"+data+"</script>");
                        }
                    });


                    //
                    // // var script = document.createElement('script');
                    // let script = $("<script></script>");
                    // script.on('load', function(){
                    //     console.log('test');
                    //     console.log(this);
                    //
                    // });
                    // script.attr('src', navigationItem.js);
                    //
                    // contentElement.append(script);

                    // document.head.appendChild(script);
                }
            },
            error: function(){
                alert('Couldn\'t load the page ' + page);
            }
        });


    }
}
let navigator;
$(function(){
    navigator = new Navigator();
    // console.log('hi');
});