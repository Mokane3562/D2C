app.factory('course_assignments_request', ['$q', '$http', function($q,$http){
	return function(crn){
		var deferred= $q.defer();
		$http({
			method: 'GET',
			url:'/d2c/course_inst/'+crn+'/assignments',
			headers: {
				'access-control-allow-origin': '*'
			}
		}).then(function(response){
			deferred.resolve(response);
		},function(response){
			deferred.reject(response);
		});
		return deferred.promise;
	}
}]);