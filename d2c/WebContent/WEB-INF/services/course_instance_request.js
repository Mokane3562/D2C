app.factory('course_instance_request',['$q','$http', function($q, $http){
	return function(crn){
		var deferred = $q.defer();
		$http({
			method: 'GET',
		    url: '/d2c/course_inst/'+crn,
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