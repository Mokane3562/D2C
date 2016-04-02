app.factory('courseByID_request', ['$q', '$http', function($q,$http){
	return function(refId){
		var deffered= $q.deffer();
		$http({
			method: 'GET',
			url:'/course_inst/refid/'+refId,
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