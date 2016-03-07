/**
 * 
 */
app.factory('java_request',[ '$q', '$http', function($q, $http){
	return function(code, user, path){
		var deferred = $q.defer();
		console.log('/d2c/code/'+user+'/'+path.split("/").join("_")+'/java');
		$http({
			method: 'POST',
			url: '/d2c/code/'+user+'/'+path.split("/").join("_")+'/java',
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