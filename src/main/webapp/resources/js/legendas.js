var L = {

    initBase: function() {
       	var centerWideContentImages = function() {
    		$(".sys-image-wide").each(function(index, item) {
	    		var width = parseInt($('body').innerWidth());
	    		if (width > 1920) {
	    			width = 1920
	    		}
	    		var left = (width - 910) / 2;
	    		$(item).css({'width': width + 'px', 'left': - left});	
    		});  		
    	};
    	centerWideContentImages();
    	
    	$(window).resize(function() {
    		centerWideContentImages();
    	});
    	
    	L.initMap("#map");
    	L.initSocialLogin();
    },
    
	initSocialLogin: function() {
		$(document).on("click", ".sys-social-login", function() {
            var service = $(this).attr("data-service");
            var urlToRedirect = window.location.href;
            
            Util._postBaseUrl("/social_login/", { action: "social-login", serviceName: service, urlToRedirect: urlToRedirect }, function(response) {
				var opts = 'status=1' + ',width=800,height=500,top=' + ($(window).height() - 500) / 2 + 
																',left=' + ($(window).width()  - 800)  / 2;
				var url = response.data['url'];
				window.open(url, 'facebook', opts);
			});
    		return false;
    	});
	},
    
    initAboutPage: function() {
    	L.initFancyBox();
    },
    
    initMainPage: function() {
    	L.initMainImageSliderPanel();
    	
    	var mainImagePhotoPanel = $(".mp-main-image-photo");
    	mainImagePhotoPanel.slick({
		  infinite: true,
		  speed: 300,
		  slidesToShow: 2,
		  slidesToScroll: 2,
		  centerMode: true,
		  variableWidth: true
		});
    	
    	L.initFancyBox();
    	
    	C.initLoadable({ 
    		'action' : "load-posters",
    		'button' : ".sys-load-poster",
            'container' : "#posterContainer",
            'loadContainer' : "#loadPosterContainer"
        });
    	
    	C.initLoadable({ 
    		'action' : "load-news",
    		'button' : ".sys-load-news",
            'container' : "#newsContainer",
            'loadContainer' : "#loadNewsContainer"
        });
    },
    
    initMenuPage: function() {
    	$(".menu-page").on("click", ".sys-category", function() {
    		Util.postOnAjax({action: "update", id: $(this).attr("cid")});
    		
    		return false;
    	});
    	
    	L.initFancyBox();
    },
    
    initPosterPage: function() {
    	L.initMainImageSliderPanel();  	
    	L.initFancyBox();    	
    	C.initLoadable();
    },
    
    initCommentsPage: function() {	
    	C.initLoadable();
    },
    
    initMainImageSliderPanel: function() {
    	var mainImageSliderPanel = $(".mp-main-image-slider");
    	if(mainImageSliderPanel.length) {
	    	mainImageSliderPanel.slick({
	      	  arrows: false,
	    	  autoplay : true,
	    	  autoplaySpeed: 3000,
			  infinite: true,
			  speed: 300,
			  slidesToShow: 1,
			  centerMode: true,
			  variableWidth: true
			});
    	}
    },

    initNewsList: function() {
    	C.initLoadable();
    },
    
    initGalleryPage: function() {
    	var area = $(".gallery-page");
    	
    	var initGallery = function() {
        	var showPhoto = function(albumId) {
    			var openPopup = function(data) {
    				var template = area.find(".sys-photo-popup").clone();
    				var largeSlider = template.find(".large-photo-slider");
    				var largeContainer = template.find(".large-photo-container");
    				var previewSlider = template.find(".preview-photo-slider");
    				var previewContainer = template.find(".preview-photo-container");
    				
    				$.each(data, function(index, item) {
    					var imageLarge = $("<img/>").attr("src", item.imageLarge);
    					largeContainer.append($("<div class='ts-image'/>").attr("pid", item.id).append(imageLarge));
    					
    					if (data.length > 1) {
        					var imageSmall = $("<img/>").attr("src", item.imageSmall);
        					previewContainer.append($("<a class='ts-preview' href='#'/>").attr("pid", item.id).append(imageSmall));
    					}
    				});
    				
    				var initSliders = function() {
    					var largeSettings = {
    						container: ".large-photo-container",
    						scrollPrev: ".scoll-left",
    						scrollNext: ".scoll-right",
    						element: ".ts-image",
    						countScroll: 1,
    						round: true
    					};
    					var scollTo = L.initScrollPanel(largeSlider, largeSettings);
    					
    					var previewSettings = {
    						container: ".preview-photo-container",
    						scrollPrev: ".scoll-left",
    						scrollNext: ".scoll-right",
    						element: ".ts-preview",
    						countScroll: 1,
    						round: true
    					};
    					L.initScrollPanel(previewSlider, previewSettings);
    					
    					previewSlider.find(".ts-preview").on("click", function() {
    						var pid = $(this).attr("pid");
    						scollTo(".ts-image[pid=" + pid + "]");

    						return false;
    					});
    				};
    				
    				var calcSizeTemplate = function() {
    					var windowWidth = $(window).width();
    					var windowHeight = $(window).height();
    					var height = windowHeight - 0.1 * windowHeight;
    					var width = Math.min(height * 16/9, windowWidth - 0.1 * windowWidth);
    					template.css({width: width + "px"});
    				};
    				var calcHeights = function() {
    					largeContainer.parents(".ts-photo-popup").height($(window).height() - 20);
    					var height = previewContainer.height();    					
    					var heightContainer = largeContainer.parents(".ts-photo-popup").height();    					
    					template.find(".large-photo-slider").height(heightContainer - height);
    					
    					largeContainer.find(".ts-image").each(function(index, element) {
    						var margin = ($(element).height() - $(element).find("img").height()) / 2;
    						$(element).css("margin-top", margin);
    					});
    				};
    				$(window).off(".photo-popup").on("resize.photo-popup", function() {
    				     largeSlider.height("auto");
    				     largeSlider.find(".ts-image, .scoll-left, .scoll-right").css("opacity", "0");
    				     
    				     calcSizeTemplate();
    				     $.fancybox.update(popup);
    				     
    				     largeSlider.stopTime("resize").oneTime(100, function() {
    				    	 largeSlider.find(".ts-image, .scoll-left, .scoll-right").css("opacity", "1");
    				     }); 				     
    				});
    				calcSizeTemplate();
    				
    				var settings = {
    					closeBtn: true,
    					padding: 0,
    					margin: 10,
    					scrolling: "no",
    					autoResize: false,
    					beforeShow: function() {
    						$(".fancybox-overlay").css({opacity: 0.0});
    					},
    					afterShow: function() {
    						$("html").addClass("gallery");
    						initSliders();
    						$(".fancybox-overlay").animate({opacity: 1.0}, 400);
    					},
    					afterClose: function() {
    						$("html").removeClass("gallery");
    					},
    					onUpdate: function() {
    						calcHeights();
    					}
    				};
    				
    				var popup = $.fancybox(template, settings);
    			};
    			
    			Util._postBaseUrl(albumId + "/get-photo/", {}, function(data) {
    				if(data.length) {
    					openPopup(data);
    				}
    			});
    		};
    		
        	area.on("click", ".sys-gallery", function() {
        		var albumId = $(this).parents(".sys-item").attr("aid");
        		showPhoto(albumId);

        		return false;
        	});
        	
        	area.on("click", ".sys-video", function() {
        		var item = $(this).parents(".sys-item");
        		var iFrame = item.find("iframe");
        		var src = iFrame.attr("src");
        		
        		iFrame.attr("src", src + "&autoplay=1");
        		iFrame.fadeIn();
        		item.find(".sys-hide").fadeOut();  
        		
        		return false;
        	});
    	};
    	initGallery();
    	
    	C.initLoadable();
    },
    
    initMap: function(mapElement) {
	    if ($(mapElement).length) {
	    	var loadMap = function() {
	    		var address = $(mapElement).attr("address");
	    		
	    		var geocoder = new  google.maps.Geocoder();
    		    geocoder.geocode({'address': address}, function(results, status) {
    		    	if (status == google.maps.GeocoderStatus.OK) {
    		    		var markerPosition = results[0].geometry.location;
    		    		var center = results[0].geometry.location;
    		    		
    		            var myOptions = {
			                zoom: 17,
			                scrollwheel: false,
			                center: center,
			                navigationControl: true,
			                navigationControlOptions: {
			                    style: google.maps.NavigationControlStyle.ZOOM_PAN
			                },

			                styles: [{stylers: 
	    						[{ saturation: -100 }]
	    					}],
			                
			                mapTypeControl: true,
			                mapTypeControlOptions: {
			                    style: google.maps.MapTypeControlStyle.DROPDOWN_MENU
			                },
			                streetViewControl: true,
			                mapTypeId: google.maps.MapTypeId.ROADMAP
			            };
    		            
    		            var map = new google.maps.Map(document.getElementById(mapElement.split('#')[1]), myOptions);
    		            
    		            var marker = new google.maps.Marker({
    		                position: markerPosition,
    		                map: map,
    		                icon: "/resources/img/web/marker.png",
    		                animation: google.maps.Animation.DROP,
//    		                title: $(mapElement).attr('data-description')
    		            });
    		            
    		            google.maps.event.addListener(marker, 'click', function() {
    		                infowindow.open(map, marker);
    		            });
    		    	}  		    
    		    });
	        }   	    
	    	loadMap();
	    	
	        $(window).resize(function() {
	        	loadMap();
	        });
	    }
    },
    
    initFancyBox: function() {
    	$(".sys-image").fancybox({
			scrolling: "no",
			autoResize: false,
    		maxWidth: 950,
    		maxHeight: $(window).height() - 130,
    		fitToView: false,
    		width: '950px',
    		height: 'auto',
    		autoSize: false,
    		closeClick: false,
    		openEffect: 'none',
    		closeEffect: 'none',
    		padding: 0,
    		helpers: {
    			overlay: {
    				locked: true
    			}
    		}
        });
    },
    
    initScrollPanel: function(panel, settings) {
		var fullScreen = settings.fullScreen;
    	var countScroll = settings.countScroll ? settings.countScroll : 1;
		var container = panel.find(settings.container);
		var scrollPrev = panel.find(settings.scrollPrev);
		var scrollNext = panel.find(settings.scrollNext);
		var elements = container.find(settings.element);
		var horizontal = settings.horizontal != null ? settings.horizontal : true; 
		var round = settings.round != null ? settings.round : true;
		
		var getSize = function(e) {
			var size;
			if (e) {
				if(horizontal) {
					size = e.getBoundingClientRect().width;
				} else {
					size = e.getBoundingClientRect().height;
				}
			}
			return size;
		};
		var sizeElement = function() {
			console.log("sizeElement=" + getSize(elements[0]));
			return getSize(elements[0]);
		};
		var sizeFull = function() {
			return sizeElement() * elements.length;
		};
		var sizeContainer = function() {
			console.log("container=" + getSize(container[0]));
			return getSize(container[0]);
		};
		
		var calcSizeContainer = function() {
			if(container.is(":visible")) {
				scrollPrev.removeClass("hidden");
				scrollNext.removeClass("hidden");
				if(!round) {
					scrollPrev.addClass("hidden");
				}
				
				if(sizeFull()) {
					if (sizeFull() <= sizeContainer()) {
						scrollPrev.addClass("hidden");
						scrollNext.addClass("hidden");
					}
					
					elements.css(horizontal ? "left" : "top", 0);
					return true;
				}
			}
			return false;
		};
		var success = calcSizeContainer();
		if(!success) {
			panel.oneTime(300, function() {
				calcSizeContainer();
			});
		}
		
		$(window).resize( function() {
			elements.find("img").css("width", "auto");
			calcSizeContainer();
			elements.find("img").css("width", "100%");
		});
		
		elements.css("position", "relative");
		
		var getLeftTop = function() {
			return horizontal ? "left" : "top";
		};
		var getScrollParam = function(value) {
			return horizontal ? {"left" : value} : {"top" : value};
		};
		
		var getPrevMaxCountScroll = function() {
			var offset = parseInt(elements.first().css(getLeftTop()).replace("px", ""));
			if(isNaN(offset)) {
				offset = 0;
			}
			
			var maxCountScroll = countScroll;
			while(maxCountScroll > 0) {
				if(offset + sizeElement() * maxCountScroll <= 0) {
					break;
				}
				maxCountScroll--;
			}
			return maxCountScroll;
		};
		var getNextMaxCountScroll = function() {
			var offset = parseInt(elements.first().css(getLeftTop()).replace("px", ""));
			if(isNaN(offset)) {
				offset = 0;
			}
			
			var maxCountScroll = countScroll;
			while(maxCountScroll > 0) {
				if(sizeElement() * maxCountScroll - offset <= sizeFull() - sizeContainer()) {
					break;
				}
				maxCountScroll--;
			}
			return maxCountScroll;
		};
		
		var runingScroll = false;
		scrollPrev.unbind("click").click(function() {
			if(runingScroll) return false;
			
			var maxCountScroll = getPrevMaxCountScroll();
			if(maxCountScroll == 0) {
				if(round) {
					container.prepend(elements.last());
					elements = container.find(settings.element);
					elements.css(getScrollParam("-=" + sizeElement() + "px"));
					maxCountScroll = getPrevMaxCountScroll();
				} else {
					return false;
				}
			}
			
			runingScroll = true;
			var param = getScrollParam("+=" + sizeElement() * maxCountScroll + "px");
			elements.animate(param, 200, null, function() {
				scrollPrev.removeClass("hidden");
				scrollNext.removeClass("hidden");
				if(!round) {
					if(getPrevMaxCountScroll() == 0) {
						scrollPrev.addClass("hidden");
					}
					if(getNextMaxCountScroll() == 0) {
						scrollNext.addClass("hidden");
					}
				}
				runingScroll = false;
			});
			
			if(typeof settings.onScrollPrev == "function") {
				settings.onScrollPrev();
			}
			return false;
		});
		scrollNext.unbind("click").click(function() {
			if(runingScroll) return false;;
			
			var maxCountScroll = getNextMaxCountScroll();
			if(maxCountScroll == 0) {
				if (round) {
					container.append(elements.first());
					elements = container.find(settings.element);
					elements.css(getScrollParam("+=" + sizeElement() + "px"));
					maxCountScroll = getNextMaxCountScroll();
				} else {
					return false;
				}
			}
			
			runingScroll = true;
			var param = getScrollParam("-=" + sizeElement() * maxCountScroll + "px");
			elements.animate(param, 200, null, function() {
				scrollPrev.removeClass("hidden");
				scrollNext.removeClass("hidden");
				if(!round) {
					if(getPrevMaxCountScroll() == 0) {
						scrollPrev.addClass("hidden");
					}
					if(getNextMaxCountScroll() == 0) {
						scrollNext.addClass("hidden");
					}
				}
				runingScroll = false;
			});
			
			if(typeof settings.onScrollNext == "function") {
				settings.onScrollNext();
			}
			return false;
		});
		
		var scollTo = function(selector) {
			if(runingScroll) return false;;
			
			var element = container.find(selector);
			
			var offset = parseInt(element.css(getLeftTop()).replace("px", ""));
			if(isNaN(offset)) {
				offset = 0;
			}
			
			var i = 0;
			while(i < elements.length && elements[i] != element[0]) i++;
			
			var offsetScroll = offset + i * sizeElement();
			console.log("offsetScroll = " + offsetScroll + " offset = " + offset + " i * size = " + (i * sizeElement()));
			
			runingScroll = true;
			var param = getScrollParam("-=" + offsetScroll + "px");
			elements.animate(param, 300, null, function() {
				if(!round) {
					scrollPrev.removeClass("hidden");
					scrollNext.removeClass("hidden");
				
					if(getPrevMaxCountScroll() == 0) {
						scrollPrev.addClass("hidden");
					}
					if(getNextMaxCountScroll() == 0) {
						scrollNext.addClass("hidden");
					}
				}
				runingScroll = false;
			});
		};
		return scollTo;
	},
};