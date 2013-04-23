function SignupCtrl($scope,$http,sharedService) {
	
	$scope.signupUser = {};
	
	$scope.signup = function() {
		console.log('logging in');
		
		$http({
			url : baseUrl + '/signup',
			data : $scope.signupUser,
			method : 'POST',
			headers : {'Content-Type':'application/json; charset=UTF-8'}
		}).success($scope.signupSuccess);
	};
	
	$scope.signupSuccess = function(data, status, headers, config) {
		if( !data.success )
		{
			alert(data.message);
		}
		else
		{
			sharedService.braodcastItem(data);
			window.location.href = $scope.currentPage;
		}
	};
}