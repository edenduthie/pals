function BaseCtrl($scope,$http,sharedService) 
{	
	var baseCtrl = {};
	
    $scope.currentPage = '#!/home';
    
    $scope.$on('handleBroadcast', function() {
        $scope.user = sharedService.item;
        console.log($scope.user);
    });
    
	$http({
		url : baseUrl + '/user',
		method : 'GET'
	}).success(function(data, status, headers, config) {
		if( data.success ){
			$scope.user = data;
		}
	});
	
	baseCtrl.loadUserResult = function(data, status, headers, config) {
		if( data.success ){
			$scope.user = data;
		}
	};
}