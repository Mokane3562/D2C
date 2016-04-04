app.factory('course_register_request',['$q','$http', function($q, $http){
	return function(crn, user, role){
		var deferred = $q.defer();
		$http({
			method: 'POST',
		    url: '/d2c/course_inst/'+crn+'/register/'+user+'/as/'+role,
		    headers:{
		    	'access-control-allow-origin': '*',
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