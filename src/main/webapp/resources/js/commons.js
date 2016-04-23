var C = {
	initialized: {},
	
	init: function() {
		
	},
	
	initEditImage: function(panel, settings) {
		if(panel.attr("cropUid")) {
        	C.initialized[panel.attr("cropUid")].destroy();
        }
		
		if(!settings) settings = {};
		if(!settings.preview) settings.preview = {}
		if(!settings.maxWidth) settings.maxWidth = 200;
		if(!settings.maxHeight) settings.maxHeight = 299;
		//if(!settings.cropWidth) settings.cropWidth = 80;
		//if(!settings.cropHeight) settings.cropHeight = 80;
		if(!settings.cropImageClass) settings.cropImageClass = "";
		if(!settings.script) settings.script = "";
		if(!settings.objectId) settings.objectId = 0;
		if(!settings.dirImage) settings.dirImage = "";
		if(!settings.addParams) settings.addParams = {};
		if(!settings.setImage) settings.setImage = false;
		
		var cropData = {};
		var editImage = false;
		var getCropData = function() {
			var scaleX = cropData.scaleX;
	        var scaleY = cropData.scaleY;
	        var x = ((cropData.coords.x * scaleX) | 0);
	        var y = ((cropData.coords.y * scaleY) | 0);
	        var width = Math.min(((cropData.coords.w * scaleX) | 0), cropData.oldWidth - x)
	        var height = Math.min(((cropData.coords.h * scaleY) | 0), cropData.oldHeight - y);
	        return {"x": x,
	            	"y": y,
	            	"width": width,
	            	"height": height
	            	};
		};
		var onSave = function(params, callback, area, urlImage ) {
			if(area) {
				panel = area;
			}
			var url = urlImage ? urlImage : scale_image.find("img").attr("src");
	        
	        Util.showLoadPanel();
	        var args = getCropData();
	        if(params) {
	        	args = $.extend(args, params);
	        }
	        args = $.extend(args, settings.addParams);
	        Util._postBaseUrl(settings.script, args, function(xml) {
	            if(callback) {
	            	callback($(xml).find("image").text(), $(xml).find("error").text());
	            } else {
	            	Util.reloadCurrentPage();
	            }
	        });
	        return false;
		};
		var onChange = function(url) {
			var img = scale_image.find("img");
			img.css({width: "auto", height: "auto"});
	    	$(img).attr("src", url).bind("load", function() {
	    		setImage(img);
	        });
	    };
	   
	    var scale_image = panel.find(settings.cropImageClass);
	    var showPreview = function(coords) {
	    	var img = scale_image.find("img");
	        var width = img.width();
	        var height = img.height();
	        
	        $.each(settings.preview, function(key, value) {
	        	var rx = value.width / coords.w;
		        var ry = value.height / coords.h;
		        $(value.nameClass).find("img").css({
		            width: Math.round(rx * width) + 'px',
		            height: Math.round(ry * height) + 'px',
		            marginLeft: '-' + Math.round(rx * coords.x) + 'px',
		            marginTop: '-' + Math.round(ry * coords.y) + 'px'
		        });
	        });
	        
	        cropData.coords = coords;
	    };
	    var setImage = function(img, width, height) {
	    	scale_image.css("width", settings.maxWidth + "px");
	        scale_image.css("height", settings.maxHeight + "px");
	        
	        //panel.find(".note:eq(1)").hide();
	        if (!width) {
	            width = img.width();
	        }
	        if (!height) {
	            height = img.height();
	        }
	        var oldHeight = height;
	        var oldWidth = width;
	        cropData.oldHeight = oldHeight;
	        cropData.oldWidth = oldWidth;
	        
	        if (height > settings.maxHeight || width > settings.maxWidth) {
	            if (height > width) {
	                width = (settings.maxHeight / height * width);
	                height = settings.maxHeight;
	                if (width > settings.maxWidth) {
	                    height = (settings.maxWidth / width * height);
	                    width = settings.maxWidth;
	                }
	            } else {
	                height = (settings.maxWidth / width * height);
	                width = settings.maxWidth;
	                if (height > settings.maxHeight) {
	                    width = (settings.maxHeight / height * width);
	                    height = settings.maxHeight;
	                }
	            }
	            img.css("width", width + 'px')
	                    .css("height", height + 'px');
	        }
	        var scaleX = oldWidth / width;
	        var scaleY = oldHeight / height;
	        
	        cropData.scaleX = scaleX;
	        cropData.scaleY = scaleY;
	        img.show();
	        
	        $.each(settings.preview, function(key, value) {
	        	$(value.nameClass).attr('src', img.attr("src"));
	        });
	        panel.attr("cropUid", Util.randomID(8));
	        C.initialized[panel.attr("cropUid")] = $.Jcrop(img, {
	            onChange: showPreview,
	            onSelect: showPreview,
	            setSelect: [ 0, 0, 
	                        settings.cropWidth > 0 ? settings.cropWidth : oldWidth, 
	            			settings.cropHeight > 0 ? settings.cropHeight : oldHeight],
	            aspectRatio: settings.cropHeight > 0 ? settings.cropWidth / settings.cropHeight : 0
	        });
	        editImage = true;
	        scale_image.show();
	        panel.find(".empty-image").hide();
	    };
	    if(settings.setImage) {
	    	var img = scale_image.find("img");
	    	var newImg = new Image();
            $(newImg).load(function() {
            	setImage(img, this.width, this.height);
            }).attr('src', img.attr("src"));
	    }
	    
	    var hasEditImage = function() {
	    	return editImage;
	    }
	    
	    return {onSave: onSave, 
	    		onChange: onChange, 
	    		setImage: setImage, 
	    		hasEditImage: hasEditImage, 
	    		getCropData: getCropData,
	    		panel: panel
	    		};
	},
	
	getPlayerCode: function(urlVideo, width, height) {
		var htmlPlayer;
		if(urlVideo.match(/youtube\.com\/watch/)) {
			var videoId = urlVideo.match(/watch\?v=([^&]*)/)[1];
			htmlPlayer = '<iframe width="' + width + '" height="' + height + '" src="http://www.youtube.com/embed/' + videoId + '?rel=0&autoplay=1" frameborder="0" allowfullscreen></iframe>';
		} else if(urlVideo.match(/vimeo.com/)) {
			var videoId = urlVideo.match(/vimeo.com\/([^\/]*)/)[1];
			htmlPlayer = '<iframe src="http://player.vimeo.com/video/' + videoId + '?autoplay=1" width="' + width + '" height="' + height + '" frameborder="0" webkitAllowFullScreen mozallowfullscreen allowFullScreen></iframe>';
		} else {
			var htmlPlayer = '<embed width="' + width + '" height="' + height + '" flashvars="file=' + urlVideo + '&backcolor=000000' + 
				'&frontcolor=FFFFFF&lightcolor=990000&screencolor=000000' + 
				'&icons=false&controlbar=over&autostart=true" allowfullscreen="true" allowscriptaccess="always"' +
				'quality="high" bgcolor="7" src="/swf/player.swf" type="application/x-shockwave-flash"/>';
		}
		return $(htmlPlayer);
	},
	
	getAudioPlayerCode: function(urlAudio, width, height) {
		var htmlPlayer = '<embed width="' + width + '" height="' + height + '" flashvars="file=' + urlAudio + '&autostart=true" ' +
			'allowfullscreen="true" allowscriptaccess="always"' +
			'quality="high" bgcolor="7" src="/swf/player_sound.swf" type="application/x-shockwave-flash"/>';
		return $(htmlPlayer);
	},
	
	initRemoveable: function(elements, options) {
		var loc = Util.getLocation();
        var settings = $.extend( {
            'title' : "Вы уверены что хотите удалить элемент?",
            'url' : (loc[loc.length - 1] == '/' ? "" : "/") + "remove"
        }, options);

        var removeRequest = function(el) {
            Util._postBaseUrl(settings.url + "/" + el.attr("iid"), {}, function(response) {
                if(response.status != 'error') {
                    if (response.redirect) {
                        var redirect = response.redirect;
                    }
                    if (redirect) {
                        window.location = redirect;
                        return;
                    }
                    el.remove();
                } else {
                    alert("Произошла ошибка");
                }
            });
        };

        elements.each(function() {
	        var el = $(this);
	
	        el.on("click", ".sys-remove", function(e){
	            $.SmartMessageBox({
	                title : settings.title,
	                content : settings.content,
	                buttons : '[Нет][Да]'
	            }, function(ButtonPressed) {
	                if (ButtonPressed === "Да") {
	                    removeRequest(el);
	                }
	            });
	
	            return false;
	        });
	    });
	},
	
	initSortable: function(area) {
		area.on("click", ".sys-sort-up, .sys-sort-down", function() {
			var action;
			var targetId;
			var item = $(this).parents(".sys-item");
			var id = item.attr("iid");
			
			if($(this).hasClass("sys-sort-up")) {
				action = "sort-up";
				targetId = item.prev().attr("iid");
			} else {
				action = "sort-down";
				targetId = item.next().attr("iid");
			}
			
			Util.postOnAjax({action: action, id: id, targetId: targetId});
			return false;
		});
	},

	initLoadable: function(options) {
		var settings = $.extend( {
			'action' : "load-page",
            'button' : ".sys-load",
            'pageAttr' : "next-page",
            'container' : "#container",
            'loadContainer' : "#loadContainer",
            'item' : ".sys-item"
        }, options);

		$(settings.loadContainer).css("display", "none");
		
		$(document).on("click", settings.button, function() {
    		var page = $(this).attr(settings.pageAttr);
    		
    		Util.postOnAjax({action: settings.action, page: page}, function() {
    			$(settings.loadContainer).find(settings.item).insertAfter($(settings.container + " " + settings.item).last());
    			
    			if (typeof settings.callback == "function") {
    				settings.callback();
    			}
    		});
    		return false;
    	});
	},
	
	initAutocomplete: function(elements, options) {
		var settings = $.extend( {
            'idText' : false,
            'multiple' : false,
            'type' : "",
            'params' : {}
        }, options);

        var properties = {
            multiple: settings.multiple ? true : false,
            allowClear: true,
            placeholder: " ",
            formatSearching: function () { return "Загрузка..."; },
            formatNoMatches: function () { return "Значений не найдено"; },
            query: function (query) {
                var postParams;
                if(settings.params) {
                    if(typeof settings.params == 'function') {
                        postParams = settings.params();
                    } else {
                        postParams = settings.params;
                    }
                } else {
                    postParams = {};
                }
                postParams["q"] = query.term;
                Util._postBaseUrl("/autocomplete/" + settings.type + "/find", postParams, function(response) {
                    var data = {results: []};
                    $.each(response, function(key, val) {
                        data.results.push({id: (settings.idText ? val.text : val.id), text: val.text, value: val.value});
                    });
                    query.callback(data);
                });
            },
            initSelection : function (element, callback) {
                var val = element.val();
                if(val.length > 0) {
                    if(settings.idText) {
                        var values = val.split(",");
                        var data = new Array();
                        $.each(values, function(i, v) {
                            if($.trim(v) != "") {
                                data.push({id: v, text: v});
                            }
                        });
                        callback(data);
                    } else {
                        var postParams;
                        if(settings.params) {
                            if(typeof settings.params == 'function') {
                                postParams = settings.params();
                            } else {
                                postParams = settings.params;
                            }
                        } else {
                            postParams = {};
                        }
                        postParams["ids"] = val.split(",");

                        Util._postBaseUrl("/autocomplete/" + settings.type + "/get", postParams, function(response) {
                            var data = response;
                            if(!settings.multiple) {
                                data = data.pop();
                            }
                            callback(data);
                        });
                    }
                }
            }
        };

        if(settings.formatResult) {
            properties["formatResult"] = settings.formatResult;
        }
        if(settings.formatSelection) {
            properties["formatSelection"] = settings.formatSelection;
        }
        if(settings.addNewValue) {
            properties["createSearchChoice"] = function (term) {
                return {id: $.trim(term), text: $.trim(term), add: true};
            };
            properties["formatResult"] = function(object, container, query) {
                if(!object.add) {
                    return object.text;
                }
                return object.text + " (Добавить)";
            };
            properties["selectOnBlur"] = true;
        }

        elements.each(function() {
            $(this).select2(properties);
        });
    }
}
$(C.init());

