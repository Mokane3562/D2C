/**
 * 
 */
app.factory('c_compile_request', '$q', '$http',[function($q, $http){
	return function(code, user, path){
		var deferred = $q.defer();
		$http({
			method: 'GET',
			url: 'localhost.com:8080/'
		}).then(function success(response){
			
		}, function error(){
			
		});
	};
}]);