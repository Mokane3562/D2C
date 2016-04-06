/**
 * 
 */
app.factory('java_request',[ '$q', '$http', function($q, $http){
	return function(code, user, main){
		var deferred = $q.defer();
		$http({
			method: 'POST',
			url: '/d2c/code/'+user+'/java/'+main,
			headers: {
				'Content-Type': 'application/json;charset=utf-8;',
				'accept': 'text/plain',
				'access-control-allow-origin': '*'
			},
			data: code
		}).then(function success(response){
			console.log("success up to http");
			deferred.resolve(response);
		}, function error(errors){
			console.log("failure at http");
			deferred.reject(errors);
		});
		console.log("finished sending request");
		return deferred.promise;
	};
}]);