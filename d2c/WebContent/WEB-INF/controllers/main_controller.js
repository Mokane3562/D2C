/**
 * Every service you use will need to be injected.
 * To do so, add the name as a string to the start of the array,
 * and add the name as a variable to the function.
 */
app.controller('main_controller',['$scope', '$location', 'example_service', 'c_compile_request',
                          function($scope, $location, example_service, c_compile_request){
	console.log("main controller loading");
	$scope.$watch( function () { return $location.path(); }, function (path) {
	    $scope.path = path;
	});
	$scope.c_compile_function = function(){
		var code = {
			"text": document.getElementById("textcodebox").value
		};
		var path = document.getElementById("path").value;
		var user = document.getElementById("user_name").value;
		c_compile_request(code, user, path).then(
			function(response){
				console.log("success");
				$scope.output = response.data;
			},
			function(errors){
				console.log("failure");
				$scope.output = errors.data;
			}
		);
		console.log("c_compile_function starting");
	};
	$scope.courseList = [];
	$scope.getCourseList = function(){
		var user = document.getElementById("user_name").value;
		var password = document.getElementById("user_name").value;
		getMeMYMotherFuckingCourses(user, password).then(
				function(response){
					$scope.courseList = response.data.courseList;
				},
				function(errors){
					//TODO deal with the error
				}
		);
	}
	$scope.courseHandler = function(course){
		//TODO something fucking useful with a course i guess?
	}
	console.log("main controller loaded");
}]);