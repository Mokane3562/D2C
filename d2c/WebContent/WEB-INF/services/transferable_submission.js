app.factory('transferable_submission',[function(){
	return function(grade, time_submitted, account, assignment){
		return {
			'grade': grade,
			'time_submitted': time_submitted,
			'account': account,
			'assignment': assignment
		};
	};
}]);