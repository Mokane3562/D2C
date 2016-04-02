app.factory('course_info_request', ['$q', '$http', function($q,$http){
	return function(crn){
		var deffered= $q.deffer();
		$http({
			method: 'GET',
			url:'/course_inst/'+crn,
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