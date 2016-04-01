app.factory('roles_request',['$q','$http', function($q, $http){
	return function(user_auth, user_name){
		var deferred = $q.defer();
		$http({
			method: 'GET',
			url: '/d2c/account/'+user_name+'/roles',
			headers:{
				'access-control-allow-origin': '*',
				'Authorization': "Basic " + user_auth				
			}
		}).then(
		function(response){
			deferred.resolve(response);
		},
		function(errors){
			deferred.reject(errors);
		});
		return deferred.promise;
	}
}]);