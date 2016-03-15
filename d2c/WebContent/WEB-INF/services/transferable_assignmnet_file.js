app.factory('transferable_assignment_file', [function(){
	return function(assignment, file){
		return {
			'assignment': assignment,
			'file': file
		};
	};
}]);