var Util = {
	post: function(parameters, success, error, isNotProcessResponse) {
    	return Util.postUrl(null, parameters, success, error, isNotProcessResponse);
    },
    postUrl: function(additionalUrl, parameters, success, error, isNotProcessResponse) {
    	var url = window.location.href;
        var i = url.indexOf('#');
        if (i > 0) url = url.substring(0, i);
        if(url.lastIndexOf("/") != url.length - 1) {
        	url += "/";
        }
        
        if(additionalUrl) {
        	url += additionalUrl + "/";
        }
        
        return Util.postBaseUrl(url, parameters, success, error, isNotProcessResponse);
    },
    postOnAjax: function(parameters, success, error, isNotProcessResponse) {
    	return Util.postOnAjaxUrl(null, parameters, success, error, isNotProcessResponse);
    },
    postOnAjaxUrl: function(additionalUrl, parameters, success, error, isNotProcessResponse) {
    	var url = window.location.href;
        var i = url.indexOf('#');
        if (i > 0) url = url.substring(0, i);
        if(url.lastIndexOf("/") != url.length - 1) {
        	url += "/";
        }
        
        i = url.indexOf('?');
        if (i > 0) {
        	url = url.substring(0, i) + "ajax/" + url.substring(i, url.length - 1);
        } else {
        	url += "ajax/";
        }

        if(additionalUrl) {
        	url += additionalUrl + "/";
        }
        
        return Util.postBaseUrl(url, parameters, success, error, isNotProcessResponse);
    },
    postBaseUrl: function(url, parameters, success, error, isNotProcessResponse) {
    	if (typeof parameters == "object") {
    		parameters = $.param(parameters);
    	} 
        
    	$(document).oneTime(1000, "show-loading", function(){
    		Util.showLoadPanel();
		});
    	var successWrap = function(data, textStatus, xhr) {
    		$(document).stopTime("show-loading");
    		Util.closeLoadPanel();
    		if(success) {
    			success(data, textStatus, xhr);
    		}
    	};
    	var errorWrap = function(xhr, code) {
    		Util.setStatus("Ошибка: сервер недоступен");
    		if(error) {
    			error(xhr, code);
    		}
    	};
    	$.ajax({
            type: 'POST',
            url: url,
            cache: false,
            beforeSend : function (xhr) {
                xhr.setRequestHeader('X-Ajax-Update', 'true');
                xhr.setRequestHeader('X-Ajax', 'true');
            },
            data: parameters,
            success: function(data, textStatus, xhr) {
                // firefox and chrome native JSON parsers hang on certain JSON data, so use eval
                data = eval(data);
                if(!isNotProcessResponse) {
	                $(data).each(function(i, v) {
	                    if (v.type == 'c') {
	                        $('#' + v.id).html(v.html);
	                    }
	                    if(v.title) {
	                    	$(document).find("title").text(v.title);
	                    }
	                });
                }
            	
                successWrap(data, textStatus, xhr);
            },
            error: errorWrap
        });
        return false;
    },
    /**
     * 
	 * Low level post
	 * 
	 **/
    _post: function(param, success, error, type, dataType) {
    	return Util._postUrl(null, param, success, error, type, dataType);
    },
    _postUrl: function(additionalUrl, param, success, error, type, dataType){
    	var url = window.location.href;
        var i = url.indexOf('#');
        if (i > 0) url = url.substring(0, i);
        if(url.lastIndexOf("/") != url.length - 1) {
        	url += "/";
        }
        
        if(additionalUrl) {
        	url += additionalUrl + "/";
        }
        Util._postBaseUrl(url, param, success, error, type, dataType)
    },
    _postBaseUrl: function(url, param, success, error, type, dataType) {
    	if(!type) {
    		type = "POST";
    	}
    	if(!dataType) {
    		dataType = "json";
    	}
    	
    	$(document).oneTime(1000, "show-loading", function(){
    		Util.showLoadPanel();
		});
    	var successWrap = function(data, textStatus, xhr) {
    		$(document).stopTime("show-loading");
    		Util.closeLoadPanel();
    		if(success) {
    			success(data, textStatus, xhr);
    		}
    	};
    	var errorWrap = function(xhr, code) {
    		Util.setStatus("Ошибка: сервер недоступен");
    		if(error) {
    			error(xhr, code);
    		}
    	};
    	$.ajax({
            type: type,
            dataType: dataType,
            url: url,
            data: param,
            timeout: 30000,
            beforeSend : function (xhr) {
                xhr.setRequestHeader('X-Ajax', 'true');
            },
            success: successWrap,
            error: errorWrap
        });
    }, 
    reloadCurrentPage: function() {
    	window.location.href = location.href;
    },
    setLocation: function(url) {
    	window.location.href = url;
    },
    getLocation: function() {
    	var url = window.location.href;
        var i = url.indexOf('#');
        if (i > 0) url = url.substring(0, i);
        return url;
    },
    getDomain: function() {
    	return window.location.host;
    },
    getDomainName: function() {
    	var domain = window.location.host;
    	var idx = domain.indexOf(":")
    	if(idx > 0) {
    		return domain.substring(0, idx);
    	}
    	return domain;
    },
    randomID: function(length) {
        length = length || 32;
        var id = "";
        for ( i = 0; i < length; i++ ) {
            id += Math.floor(Math.random() * 16).toString(16);
        }
        return id;
    },
    setStatus: function(status) {
    	
    },
    showLoadPanel: function() {
    	var loadPanel = $("#load-panel");
    	loadPanel.show();
    },
    closeLoadPanel: function() {
    	var loadPanel = $("#load-panel");
    	loadPanel.hide();
    },
    getUrlComponents: function() {
    	var url = location.pathname;
    	var urlComponents = new Array();
    	$.each(url.split("/"), function(key, value) {
    		if(value.trim() == "") return true;
    		urlComponents[urlComponents.length] = value;
    	});
    	return urlComponents;
    },
    getPathname: function() {
    	return location.pathname;
    },
    serialize: function(element) {
    	var params = "";
    	var tags = "input[type=text]," 
    				+ "input[type=password]," 
    				+ "input[type=hidden]," 
    				+ "input[type=checkbox]:checked," 
    				+ "input[type=radio]:checked," 
    				+ "select,"
    				+ "textarea";
    	element.find(tags).each( function() {
    		if(params.length) {
    			params += "&";
    		}
    		params += $(this).attr("name") + "=" + $(this).val();
    	});
    	params = params.replace(/\+/g, "%2B");
    	return params;
    }
};

(function($) {
    $.queryString = (function(a) {
        if (a == "") return {};
        var b = {};
        for (var i = 0; i < a.length; ++i)
        {
            var p=a[i].split('=');
            if (p.length != 2) continue;
            b[p[0]] = decodeURIComponent(p[1].replace(/\+/g, " "));
        }
        return b;
    })(window.location.search.substr(1).split('&'))
})(jQuery);