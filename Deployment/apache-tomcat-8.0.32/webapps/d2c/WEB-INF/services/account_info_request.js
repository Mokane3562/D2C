app.factory('account_info_request', ['$q', '$http', function($q, $http){
	return function(user_auth, username){
		var deferred = $q.defer();
		var url = "/d2c/account/"+username;
		$http({
			method: 'GET',
			url: url,
			headers: {
				'access-control-allow-origin': '*',
				'Authorization': "Basic " + user_auth
			}
		}).then(function(response){
			deferred.resolve(response);
		},function(response){
			deferred.reject(response);
		});
		return deferred.promise;
	}
}]);