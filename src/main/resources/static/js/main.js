(function () {
    "use strict";

    var app = angular.module("shorturlapp", []);

    /**
     * Controller for the main page. A user can enter a URL in a single field form. If the form is submitted,
     * a REST call is made to the server to get or create the short URL for the given long URL.
     */
    app.controller("ShortUrlController", ['$scope', '$http', function($scope, $http) {

        var self = this;
        var shortUrlTemplate = {
            url: ""
        };

        // Hold form errors
        this.form = {
            errors: []
        };

        // Hols form field values
        this.fields = angular.copy(shortUrlTemplate);

        // Data structure to hold the short and long URLs after we get a slug back from the server
        this.url = {
            short: "",
            long: ""
        };

        /**
         * Handler for submitting the form to get a short URL
         */
        this.submit = function() {
            self.form.errors.length = 0;

            $http({
                method: 'POST',
                url: '/api/v1/shorturl',
                data: this.fields

            }).then(function successCallback(response) {

                // Build the absolute short URL from the slug, current scheme and domain name
                var loc = window.location;
                self.url.short = loc.protocol + "//" + loc.host + "/" + loc.pathname.split('/')[1] + response.data.slug;
                self.url.long = response.data.url;
                self.fields.url = "";

            }, function errorCallback(response) {
                self.form.errors = response.data.errors;
            });
        }

    }]);

}());
