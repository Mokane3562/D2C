app.factory('transferable_submission_file',[function(){
	return function(submission, file){
		return {
			'submission': submission,
			'file': file
		};
	};
}]);