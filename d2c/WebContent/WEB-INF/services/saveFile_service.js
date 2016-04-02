app.factory('saveFile_service',['$q','$http',function($q,$http){
	return function(userName, path, code){
		var deffered= $q.deffer();
		$http({
			method:'POST',
			url: '/code/'+userName+'/'+path,
			headers: {
				'access-control-allow-origin': '*',
			},
			data: code
			
		}).then(function(response){
			deferred.resolve(response);
		},function(response){
			deferred.reject(response);
		});
		return deferred.promise;
	}	
}]);