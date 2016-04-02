app.factory('jsFile_request',['$q','$http',function($q,$http){
	return function(folder, js){
		var deffered= $q.deffer();
		$http({
			method:'GET',
			url: '/'+folder+'/'+js+'.js',
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