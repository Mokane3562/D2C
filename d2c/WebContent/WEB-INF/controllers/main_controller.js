/**
 * Every service you use will need to be injected.
 * To do so, add the name as a string to the start of the array,
 * and add the name as a variable to the function.
 */
app.controller('main_controller',['$scope', '$location', 'example_service', function($scope, $location, example_service){
	$scope.course = example_service()[0];
	$scope.submission = example_service()[1];
	$scope.compile_c = c_compile_request();
	$scope.run_c = c_run_request();
	$scope.compile_java = java_compile_request();
	$scope.run_java = java_run_request();
	$scope.$watch( function () { return $location.path(); }, function (path) {
	    $scope.path = path;
	});
}]);