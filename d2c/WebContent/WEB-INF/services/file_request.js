app.factory('file_request', ['$q','$http', function($q,$http){
	return function(fileId){
		var deferred = $q.defer();
		$http({
			method: 'GET',
			url: '/d2c/file/refid/'+fileId,
			headers: {
				'access-control-allow-origin': '*'
			}
		}).then(
			function(response){
			deferred.resolve(response);
		},function(response){
			deferred.reject(response);
		});
		return deferred.promise;
	}
}]);