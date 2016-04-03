app.factory('course_by_id_request',['$q','$http',function($q,$http){
	return function(refID){
		var deferred= $q.defer();
		$http({
			method:'GET',
			url: '/d2c/course/refid/'+refID,
			headers: {
				'access-control-allow-origin': '*',
			},
			
		}).then(function(response){
			deferred.resolve(response);
		},function(response){
			deferred.reject(response);
		});
		return deferred.promise;
	}	
}]);