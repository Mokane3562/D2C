/**
 * 
 */
app.factory('c_compile_request', '$q', '$http',[function($q, $http){
	return function(code, user, path){
		var deferred = $q.defer();
		$http({
			method: 'POST',
			url: 'localhost.com:8080/'+user+'/'+path,
			headers: {
				'Content-Type': text/plain,
				'access-control-allow-origin': '*'
			},
			data: {
				content: code
			}
		}).then(function success(response){
			deferred.resolve(response);
		}, function error(errors){
			deferred.reject(errors);
		});
		return deferred.promise;
	};
}]);