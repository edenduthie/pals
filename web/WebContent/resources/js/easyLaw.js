var easyLaw = angular.module('easyLaw', ['ngSanitize']);

easyLaw.config(['$routeProvider', function($routeProvider) {
    $routeProvider.
        when('/login', {templateUrl: baseUrl + '/resources/templates/login.html',   controller: LoginCtrl}).
        when('/signup', {templateUrl: baseUrl + '/resources/templates/signup.html',   controller: SignupCtrl}).
        when('/', {templateUrl: baseUrl + '/resources/templates/home.html',   controller: HomeCtrl}).
        otherwise({redirectTo: '/'});
}]);

easyLaw.config(['$locationProvider', function($location) {
  $location.hashPrefix('!');
}]);

easyLaw.factory('sharedService', function($rootScope) {
    var sharedService = {};
    
    sharedService.item = '';

    sharedService.braodcastItem = function(item) {
        this.item = item;
        this.broadcastItem();
    };

    sharedService.broadcastItem = function() {
        $rootScope.$broadcast('handleBroadcast');
    };

    return sharedService;
});