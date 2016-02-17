/**
 * 
 */
app.factory('example_service',[function(){
	return function(){
		return [
		    {
				"name": "Grades"
			},
			{
				"name": "Submission"
			}
		];
	};
}]);