app.factory('transferable_assignment', [function(){
	return function(number, due_date, course_instance){
		return {
			'number': number,
			'due_date': due_date,
			'course_instance': course_instance
		};
	};
}]);