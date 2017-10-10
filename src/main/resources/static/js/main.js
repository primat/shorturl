
// jQuery file handles all UI logic (on the home page)
$(document).ready(function(){

    // Cached selectors
    var cta = $(".cta").first();
    var err = $(".err").first();
    var url = $("#url");
    var shortUrl = $("#short-url");
    var form = $("#form-url-shortener");

    // Handler for submitting the URL to the shortener service
    cta.on("click", function() {
        var urlValue = url.val();

        if (urlValue !== "") {
            // Reset the page state before re-submitting a URL
            shortUrl.html("");
            err.html("").hide();

            // Make the ajax call to get a short URL
            $.ajax({
                type: "POST",
                url: "/api/v1/shorturl",
                data: { url: urlValue },
                success: function(data){
                    var loc = window.location;
                    var shortUrlString = loc.protocol + "//" + loc.host + "/" + loc.pathname.split('/')[1] + data.slug;
                    cta.attr("disabled", "disabled");
                    $('<a>',{
                        text: shortUrlString,
                        href: shortUrlString,
                        target: "_blank"
                    }).appendTo(shortUrl);
                }
            });
        }
    });

    // Handle changes in the URL form field. When the field is empty, disable the submit button
    url.keyup(function() {
        // TODO: Validate input ( i.e. $( this ).val())
        shortUrl.html("");
        err.html("").hide();

        if (url.val() === "") {
            cta.attr("disabled", "disabled");
        }
        else {
            cta.removeAttr("disabled");
        }
    });
});
