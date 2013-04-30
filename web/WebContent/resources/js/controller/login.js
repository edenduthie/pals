function LoginCtrl($scope,$http,sharedService) {
	
	$scope.loginUser = {};
	
	$scope.error = false;
	
	$scope.login = function() {
		console.log('logging in');
		
		$http({
			url : baseUrl + '/login',
			data : $scope.loginUser,
			method : 'POST',
			headers : {'Content-Type':'application/json; charset=UTF-8'}
		}).success($scope.loginSuccess);
	};
	
	$scope.loginSuccess = function(data, status, headers, config) {
		if( !data.success )
		{
			$scope.errorMessage = "Invalid username/password, please try again.";
			$scope.error = true;
		}
		else
		{
			sharedService.braodcastItem(data);
			window.location.href = $scope.currentPage;
		}
	};
}