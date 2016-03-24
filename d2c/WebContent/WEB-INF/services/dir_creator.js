app.factory('dir_creator', [function(){
	return function(name, contents, type, editor, display){
		var obj = {
			name: name,
			contents: contents,
			type: type,
			visable: visable
		};
		if(type === "file" || type === "f"){
			obj.onDblClick = function(){
				editor.setValue(obj.contents, -1);
			};
		} else {
			obj.onDblClick = function(){
				if(!visable){
					display.push.apply(display, contents);
				} else {
					
				}
			}
		}
		return obj;
	};
}]);