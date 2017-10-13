(function () {
    "use strict";

    var app = angular.module("shorturlapp", []);

    app.controller("ShortUrlController", ['$scope', '$http', function($scope, $http) {

        var self = this;
        var shortUrlTemplate = {
            url: ""
        };

        this.form = {
            errors: []
        };
        this.fields = angular.copy(shortUrlTemplate);

        // Data structure to hold the short and long URLs after we get a slug from the server
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
