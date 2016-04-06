app.factory('create_file_request',['$q','$http', function($q, $http){
	return function(transferable_file){
		var deferred = $q.defer();
		console.log(deferred);
		$http({
			method:'POST',
		    url:'/d2c/file/new',
		    headers:{
		    	'access-control-allow-origin': '*'
		    },
		    data: null
		}).then(
		function(response){
			deferred.resolve(response);
		},
		function(errors){
			deferred.reject(errors);
		});
		console.log(deferred.promise);
		return deferred.promise;
	};
}]);