var E = {
		
	
    initBase: function() {
    	$('input.datepicker').datepicker({ dateFormat: 'dd.mm.yy' });
    	$('input.timepicker').timepicker({
    		defaultTime: false,
    		showMeridian: false,
    		minuteStep: 5
    	});
    	$('.wysiwyg').each(function() {
			var self = $(this);
			self.summernote({
				height: 300, 
				toolbar: [
				            ['style', ['style', 'bold', 'italic', 'underline', 'clear']],
				            ['ul', ['ul', 'paragraph']],
				            ['color', ['color']],
				            ['height', ['height']],
				            ['misc', ['fullscreen', 'codeview']],
				            ['insert', ['link']]
				         ],
				onPaste: function(e) {
					console.log(e);
				}
			});
			self.code(self.val());
			
			self.everyTime(300, "wysiwyg", function() {
				self.val(self.code());
			});
		});
		
		$("input").first().focus();
    },
    
    initSettingsPage: function() {
    	var area = $(".settings-editor");
    	
    	E.initImagePanel(area.find(".settings-menu-image"), {
			imagesCrop: [{
				container: ".image",
				cropWidth: 0,
                cropHeight: 0
			}]
		});
    },
    
	initMainImageList: function() {
		var area = $(".sys-main_images");

		C.initSortable(area);		
		C.initRemoveable(area.find(".sys-item"));
	},
	
	initMainImagePage: function() {
		var area = $(".main-image-editor");

    	var type = area.find(".main-image input[name=type]").val();
    	var width = 0;
    	var height = 0;
    	if(type == "MAIN_IMAGE_SLIDER") {
    		width = 1100;
    		height = 450;
    	} else if(type == "MAIN_IMAGE_PHOTO") {
    		width = 550;
    		height = 550;
    	}
    	
		E.initImagePanel(area.find(".main-image"), {
			imagesCrop: [{
				container: ".image",	
				cropWidth: width,
                cropHeight: height
			}],
		});
	},
	
	initCityList: function() {
		var area = $(".sys-cities");
		
		C.initRemoveable(area.find(".sys-item"));
	},
	
	initCityPage: function() {
		var area = $(".city-editor");
		
		C.initAutocomplete($("input[name*=country]"), { type : 'country', formatResult: E.formatResultCategory});
	},
	
	initCountryList: function() {
		var area = $(".sys-countries");
		
		C.initRemoveable(area.find(".sys-item"));
	},
	
	initCountryPage: function() {
		var area = $(".country-editor");
	},
	
	initTeamList: function() {
		var area = $(".sys-teams");
		
		C.initRemoveable(area.find(".sys-item"));
	},
	
	initTeamPage: function() {
		var area = $(".team-editor");
		
		C.initAutocomplete($("input[name*=city]"), { type : 'city', formatResult: E.formatResultCategory});
		C.initAutocomplete($("input[name*=chiefInstructor]"), {
			type: 'athlete',
			formatResult: E.formatResultCategory,
			params: {
				instructor: true
			}
		});
	},

	initGymList: function() {
		var area = $(".sys-gyms");

		C.initRemoveable(area.find(".sys-item"));
	},

	initGymPage: function() {
		var area = $(".gym-editor");

		C.initAutocomplete($("input[name*=city]"), { type : 'city', formatResult: E.formatResultCategory});
		C.initAutocomplete($("input[name*=team]"), { type : 'team', formatResult: E.formatResultCategory});
		C.initAutocomplete($("input[name*=instructors]"), {
			multiple : true,
			type : 'athlete',
			formatResult: E.formatResultCategory,
			params: {
				instructor: true
			}
		});
	},

	initTariffList: function() {
		var area = $(".sys-tariffs");

		C.initRemoveable(area.find(".sys-item"));
	},

	initTariffPage: function() {
		var area = $(".tariff-editor");

		C.initAutocomplete($("input[name*=gym]"), { type : 'gym', formatResult: E.formatResultCategory});
	},

	initCouponList: function() {
		var area = $(".sys-coupons");

		C.initRemoveable(area.find(".sys-item"));
	},

	initCouponPage: function() {
		var area = $(".coupon-editor");
	},
	
	initPersonList: function() {
		var area = $(".sys-persons");
		
		C.initRemoveable(area.find(".sys-item"));
	},
	
	initPersonPage: function() {
		var area = $(".person-editor");
	},
	
	initAthleteList: function() {
		var area = $(".sys-athletes");
		
		C.initRemoveable(area.find(".sys-item"));
		C.initSortableTable(area);
	},
	
	initAthletePage: function() {
		var area = $(".athlete-editor");
		
		var topSaveButton = area.find(".sys-save-top");
		topSaveButton.off("click").on("click", function () {
			area.find("form#athlete").submit();
			return false;
		});
		
		E.initImagePanel(area.find(".athlete-image"), {
			imagesCrop: [{
				container: ".image",	
				cropWidth: 300,
                cropHeight: 300
			}]
		});
		
		C.initAutocomplete($("input[name=person]"), {
			type: 'person', 
			formatResult: E.formatResultCategory, 
			params: {
				hasNotAthlete: true
			}
		});
		C.initAutocomplete($("input[name*=registrationRegion]"), {
			type: 'registration_region', 
			formatResult: E.formatResultCategory
		});
		C.initAutocomplete($("input[name*=team]"), {
			type: 'team',
			formatResult: E.formatResultCategory
		});
	},
	
	initRegistrationRegionList: function() {
		var area = $(".sys-registration-regions");
		
		C.initRemoveable(area.find(".sys-item"));
	},
	
	initRegistrationRegionPage: function() {
		var area = $(".registration-region-editor");
	},
	
    initArticleList: function() {
		var area = $(".sys-articles");
		
		C.initRemoveable(area.find(".sys-item"));
	},
	
	initArticlePage: function() {
		var area = $(".article-editor");
		
		E.initImagePanel(area.find(".content-image"), {
			imagesCrop: [{
				container: ".image",	
				cropWidth: 0,
				cropHeight: 0,
				maxWidth: 0,
				maxHeight: 0,
			}],
			isMultiple: true,
			notNeedCrop: true
		});
		
    	E.initImagePanel(area.find(".article-image"), {
			imagesCrop: [{
				container: ".image",	
				cropWidth: 510,
                cropHeight: 400
			}]
		});
    	
    	E.initImagePanel(area.find(".article-small-image"), {
			imagesCrop: [{
				container: ".image",	
				cropWidth: 283,
                cropHeight: 400
			}]
		});
    	
    	E.initImagePanel(area.find(".article-inner-preview-image"), {
			imagesCrop: [{
				container: ".image",	
				cropWidth: 1920,
                cropHeight: 380
			}]
		});
    	
		var fillBlock = function(block, position) {
			block.find("input[name*=videoId]").attr("name", "videoId" + position);
			block.find("input[name*=link]").attr("name", "link" + position);
		};
    	E.initAddBlockPanel($(".videos"), fillBlock);
    	
    	$(".videos button[name=save]").click(function(e){
    		e.preventDefault();
    		var params = Util.serialize($(this).parents("form"));
    		Util.postOnAjax(params + "&action=save-videos");
    	});
	},
	
	formatResultCategory: function(object) {
		var level = object.value;
		var text = object.text;
		if (level < 1) {
			return "<b>" + text + "</b>";
		} else if (level < 2) {
			return "<ins style='padding-left: 30px;'>" + text + "</ins>";
		} else {
			return "<div style='padding-left: 60px;'>" + text + "</div>";
		}
	},
	
	initImagePanel: function(area, params) {
		var isMultiple = false;
		var notNeedCrop = false;
		var imagesCrop = new Array({
			imageCrop : ".crop-image",
			maxWidth : 0,
			maxHeight : 0,
			cropWidth : 0,
			cropHeight : 0
		});
		var refrash; 
		if(params) {
			if(params.imagesCrop) {
				imagesCrop = params.imagesCrop;
			}
			if(params.isMultiple) {
				isMultiple = params.isMultiple;
			}
			if(params.notNeedCrop) {
				notNeedCrop = params.notNeedCrop;
			}
			if(params.refrash) {
				refrash = params.refrash;
			}
		}
		
		var imagePanel = area.hasClass("image-panel") ? area : area.find(".image-panel");
		var type = area.find("input[name=type]").val();
		var mode = area.find("input[name=mode]").val();
		var format = area.find("input[name=format]").val();
		var id = area.find("input[name=id]").val();
		var language = area.find("input[name=language]").val();
		
		var initListImages = function() {
			area.find(".list-images").SuperBox();
		}; 
		initListImages();
		
		if(!refrash) {
			refrash = function() {
				var clear = function() {
					imagePanel.find("input[name=imageUrl]").val("");
					imagePanel.find("input[type=file]").val("");
	//				save isEncrease value on multiple file upload requests
	//				imagePanel.find("input[name='isEncrease']:checked").attr("checked", false);
					imagePanel.find("input[type=file]").change();
				};
				if(area.find(".crop-image-panel").length) {
					Util.postOnAjax({action: "refrash-crop-image", type: type}, function() {
						clear();
					});
				}
				if(area.find(".list-images").length) {
					Util.postOnAjax({action: "refrash-image", type: type}, function() {
						clear();
						initListImages();
					});
				}
			};
		}
		
		var cropImage = function(srcImage, fileName) {
			var eventsArr = new Array();
			$.each(imagesCrop, function(i, imageCrop) {
				var containerSelector = imageCrop.container ? imageCrop.container : ".image";
				
				var imageEl = imagePanel.find(containerSelector);
				if(imageEl.hasClass("croping")) {
					imageEl.attr("style", "");
					imageEl.html("<img class='crop-image'/>");
				} else {
					imageEl.addClass("croping");
				}
				imageEl.find("img.crop-image").attr("src", srcImage);
				
				eventsArr[eventsArr.length] = C.initEditImage(imagePanel, {
					preview: [],
					maxWidth: imageCrop.maxWidth ? imageCrop.maxWidth : 300,
					maxHeight: imageCrop.maxHeight ? imageCrop.maxHeight : 300,
					cropWidth: imageCrop.cropWidth,
					cropHeight: imageCrop.cropHeight,
					cropImageClass: containerSelector,
					setImage: true
				});
			});
			
			imagePanel.find(".btn.save-image").show().unbind("click").click(function() {
				var args = {type: type, format: format, id: id, language: language, "tmpFile": fileName};
				
				$.each(eventsArr, function(i, events) {
					var cropData = events.getCropData();
					args["x" + (i > 0 ? i + 1 : "")] = cropData.x;
					args["y" + (i > 0 ? i + 1 : "")] = cropData.y;
					args["width" + (i > 0 ? i + 1 : "")] = cropData.width;
					args["height" + (i > 0 ? i + 1 : "")] = cropData.height;
				})
				$(document).oneTime(100, function(){
		    		Util.showLoadPanel();
				});
				Util._postBaseUrl("/editor/upload_image/save", args, function(response) {
					Util.closeLoadPanel();
					refrash();
		        });
				
				return false;
			});
		};
		
		var datas = new Array();
		imagePanel.find('#upload').fileupload({
			url: '/editor/upload_image/',
			acceptFileTypes: /(\.|\/)(gif|jpe?g|png)$/i,
		    sequentialUploads: true,
	        dataType: 'json',
	        autoUpload: false,
	        formData: function() {
	        	var	isEncrease = imagePanel.find("input[name='isEncrease']:checked").length > 0;
	        				
	            return [ {name: "mode", value: mode}, 
	            		 {name: "type", value: type},
	            		 {name: "id", value: id},
	            		 {name: "language", value: language},
	            		 {name: "isEncrease", value: isEncrease},
	            		 {name: "format", value: format} ];
	        },
	        done: function (e, data) {
	        	doneCallback(data);
	        },
	        change: function (e, data) {
	        	var names = new Array();
	            $.each(data.files, function (index, file) {
	            	names[names.length] = file.name;
	            });
	            imagePanel.find(".input-file input[type=text]").val(names.join(", "));
	            datas = [];
	        },
		    add: function (e, data) {
		    	datas[datas.length] = data;
		    }
		});
		
		var doneCallback = function(data) {
			Util.closeLoadPanel();
			var response = data;
			if(data.result) {
				response = data.result;
			} 
	        
	        if(response.status == 'size-error') {
	        	$.SmartMessageBox({
					title : "Ошибка",
					content : "Размер не подходит",
					buttons : '[Ok]'
				});
	        	return;
	        }
	        if(notNeedCrop) {
	        	refrash();
	        } else {
	        	cropImage(response.data.path, response.data.fileName);
	        }
	    };
		
		imagePanel.find(".btn.upload").off("click").on("click", function() {
			if(datas.length > 0) {
				Util.showLoadPanel();
			
				$.each(datas, function(i, v) {
					v.submit();
				});
				datas = [];
			}
			var imageUrl = imagePanel.find("input[name=imageUrl]");
			if(imageUrl.val() != "") {
				var isEncrease = imagePanel.find("input[name='isEncrease']:checked").length > 0;
				Util._postBaseUrl("/editor/upload_image/", {mode: mode, type: type, id: id, language: language,
														imageUrl: imageUrl.val(), isEncrease: isEncrease}, function(response) {
					doneCallback(response);
		        });
			}
			return false;
		});
		
		imagePanel.on("click", ".remove-image", function() {
			var self = $(this);
			var imageType = area.find(".superbox-show").prev().attr("imageType");
			if(!imageType) {
				imageType = type;
			}
			var removeId = self.attr("iid");
			if(!removeId) {
				if(isMultiple) {
					removeId = area.find(".block-image.active").attr("iid");
					if(!removeId) {
						removeId = area.find(".superbox-show").prev().attr("iid");
					}
				} else {
					removeId = id;
				}
			}
			Util._postBaseUrl('/editor/upload_image/remove', {mode: "mode_remove", type: imageType, format: format, id: removeId, language: language}, function() {
				if(isMultiple) {
					Util.reloadCurrentPage();
				} else {
					refrash();
				}
			});
			return false;
		});
		
		if(isMultiple) {
			imagePanel.on("click", ".edit-image", function() {
				var self = $(this);
				var id = area.find(".block-image.active").attr("iid");
				Util.post(self.parents("form").serialize() + "&action=edit-image&id=" + id, function() {
					Util.reloadCurrentPage();
				});
				return false;
			});
		}
	},
	
	initAddBlockPanel: function(panel, fillBlock, initBlock, onRemove) {
		panel.find(".block:not(.new-block)").each(function(){
			var block = $(this);
			if (typeof(initBlock) == "function") {
				initBlock(block);
			}
		});
		panel.on("click", ".remove-block", function() {
			var row = $(this).parents(".block");
			var remove = function() {
				row.remove();
				
				var position = 0;
				panel.find(".block:not(.new-block)").each(function(){
					var block = $(this);
					fillBlock(block, position);
					position++;
				});
			};
			if(onRemove) {
				onRemove(row, remove);
			} else {
				remove();
			}
			
			return false;
		});
		panel.find(".wysiwyg, iframe").css("width", "100%");
		panel.find(".add-block a").click( function() {
			var newBlock = panel.find(".block.new-block").clone();
			newBlock.removeClass("new-block");
			
			var position = panel.find(".block:not(.new-block)").length;
			fillBlock(newBlock, position);
			panel.find(".block").last().after(newBlock);
			
			newBlock.find(".init-wysiwyg").each( function() {
				E.initWysiwyg($(this));
			});
			if (typeof(initBlock) == "function") {
				initBlock(newBlock);
			}
			
			panel.find(".wysiwyg, iframe").css("width", "100%");

			return false;
		});
	}
};