/**
 * Директивы
 */

var app = angular.module("app");

app.directive('ngDatepicker', function() {
    return {
        link: function(scope, element) {
            $(element).datepicker({
                dateFormat: 'dd.mm.yy',
                nextText: '',
                prevText: '',
                beforeShow: function () {
                    $(".ui-datepicker").addClass("open");
                },
                onClose: function () {
                    $(".ui-datepicker").removeClass("open");
                }
            });
        }
    };
});

app.directive('ngListSearchPanel', function() {
    return {
        templateUrl: "/resources/views/include/listSearch.html",
        restrict: 'E',
        scope: true,
        link: function(scope, element, attrs) {
            scope.newLabel = attrs.newLabel;
            scope.canAdd = attrs.canAdd === "true";
        }
    }
});

app.directive('ngCarousel', function($timeout) {
    return {
        link: function(scope, element) {
            var o = $(element),
                inited = false;

            scope.$watchGroup(["bannerList", "showBanner"], function (newValues) {
                if (newValues[0] && newValues[0].length && newValues[1]) {
                    if (!inited) {
                        inited = true;
                        $timeout(function () {
                            initCarousel();
                        });
                    }
                }
            });

            function initCarousel() {
                console.log("init owl carousel");

                var isTouch = "ontouchstart" in window;

                function preventScroll(e) {
                    e.preventDefault();
                }

                var responsive = {};

                var aliaces = ["-", "-xs-", "-sm-", "-md-", "-lg-"],
                    values = [0, 480, 768, 992, 1200],
                    i, j;

                for (i = 0; i < values.length; i++) {
                    responsive[values[i]] = {};
                    for (j = i; j >= -1; j--) {
                        if (!responsive[values[i]]["items"] && o.attr("data" + aliaces[j] + "items")) {
                            responsive[values[i]]["items"] = j < 0 ? 1 : parseInt(o.attr("data" + aliaces[j] + "items"));
                        }
                        if (!responsive[values[i]]["stagePadding"] && responsive[values[i]]["stagePadding"] !== 0 && o.attr("data" + aliaces[j] + "stage-padding")) {
                            responsive[values[i]]["stagePadding"] = j < 0 ? 0 : parseInt(o.attr("data" + aliaces[j] + "stage-padding"));
                        }
                        if (!responsive[values[i]]["margin"] && responsive[values[i]]["margin"] !== 0 && o.attr("data" + aliaces[j] + "margin")) {
                            responsive[values[i]]["margin"] = j < 0 ? 30 : parseInt(o.attr("data" + aliaces[j] + "margin"));
                        }
                    }
                }

                o.owlCarousel({
                    autoplay: o.attr("data-autoplay") === "true",
                    autoplayTimeout: o.attr("data-autoplay-timeout") ? parseInt(o.attr("data-autoplay-timeout")) : 5000,
                    navSpeed: o.attr("data-nav-speed") ? parseInt(o.attr("data-nav-speed")) : false,
                    autoplaySpeed: o.attr("data-autoplay-speed") ? parseInt(o.attr("data-autoplay-speed")) : false,
                    loop: o.attr("data-loop") !== "false",
                    items: 1,
                    mouseDrag: o.attr("data-mouse-drag") !== "false",
                    nav: o.attr("data-nav") === "true",
                    dots: o.attr("data-dots") === "true",
                    dotsEach: o.attr("data-dots-each") ? parseInt(o.attr("data-dots-each")) : false,
                    responsive: responsive,
                    navText: [],
                    onInitialized: function () {
                        if ($.fn.magnificPopup) {
                            var o = this.$element.attr('data-lightbox') !== undefined && this.$element.attr("data-lightbox") !== "gallery",
                                g = this.$element.attr('data-lightbox') === "gallery";

                            if (o) {
                                this.$element.each(function () {
                                    var $this = $(this);
                                    $this.magnificPopup({
                                        type: $this.attr("data-lightbox"),
                                        callbacks: {
                                            open: function () {
                                                if (isTouch) {
                                                    $(document).on("touchmove", preventScroll);
                                                    $(document).swipe({
                                                        swipeDown: function () {
                                                            $.magnificPopup.close();
                                                        }
                                                    });
                                                }
                                            },
                                            close: function () {
                                                if (isTouch) {
                                                    $(document).off("touchmove", preventScroll);
                                                    $(document).swipe("destroy");
                                                }
                                            }
                                        }
                                    });
                                })
                            }

                            if (g) {
                                this.$element.each(function () {
                                    var $gallery = $(this);

                                    $gallery
                                        .find('[data-lightbox]').each(function () {
                                        var $item = $(this);
                                        $item.addClass("mfp-" + $item.attr("data-lightbox"));
                                    })
                                        .end()
                                        .magnificPopup({
                                            delegate: '.owl-item [data-lightbox]',
                                            type: "image",
                                            gallery: {
                                                enabled: true
                                            },
                                            callbacks: {
                                                open: function () {
                                                    if (isTouch) {
                                                        $(document).on("touchmove", preventScroll);
                                                        $(document).swipe({
                                                            swipeDown: function () {
                                                                $.magnificPopup.close();
                                                            }
                                                        });
                                                    }
                                                },
                                                close: function () {
                                                    if (isTouch) {
                                                        $(document).off("touchmove", preventScroll);
                                                        $(document).swipe("destroy");
                                                    }
                                                }
                                            }
                                        });
                                })
                            }
                        }
                    }
                });
            }
        }
    }
});