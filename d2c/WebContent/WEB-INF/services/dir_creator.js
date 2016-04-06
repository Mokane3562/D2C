app.factory('dir_creator', [function(){
	return function(name, contents, type, editor, node){
		var obj = {
			name: name,
			contents: contents,
			parent: node.node,
			type: type,
			visable: false
		};
		if(type === "file" || type === "f"){
			obj.onDblClick = function(){
				editor.setValue(obj.contents, -1);
			};
		}
		obj.onClick = function($event){
			$event.stopPropagation();
			if(node.node === obj){
				obj.visable = !obj.visable
			}
			node.node = obj;
		};
		obj.getQualifiedName = function(){
			if(obj.name === "root"){
				return "/root";
			}
			return parent.getQualifiedName() + "/" + obj.name;
		}
		return obj;
	};
}]);