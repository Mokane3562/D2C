app.factory('css_request',['$q','$http',function($q,$http){
	return function(css){
		var deffered= $q.deffer();
		$http({
			method:'GET',
			url: '/style/'+css+'.css',
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