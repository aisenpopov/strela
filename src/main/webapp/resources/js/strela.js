var S = {

    showAndHide: function (message, duration) {
        message.show();
        setTimeout(function () {
            message.hide();
        }, duration);
    },

    scrollTo: function (position, duration) {
        $("html, body").animate({
            scrollTop: position
        }, duration);
    },

    validateForm: function (form, scroll) {
        var errors = form.find(".form-error");
        if (errors.length && scroll) {
            S.scrollTo(errors.first().offset().top - 100, 500);
        }

        return !errors.length;
    },

    initBase: function() {
        $("input.datepicker").datepicker({
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

        $(".rd-navbar .sys-change-city").off("click").on("click", function () {
            var url = window.location.href;
            if (url.indexOf("gym") > 0) {
                S.updateGyms($(this).data("cityId"));

                return false;
            }

            return true;
        });
    },

    updateGyms: function (cityId) {
        if (!cityId) {
            cityId = "";
        }
        Util.postBaseUrl("/gym/", "cityId=" + cityId, function () {
            $(".gym-list .sys-change-city").each(function () {
                var $this = $(this);
                if ($this.data("cityId") === cityId) {
                    $this.addClass("active");
                } else {
                    $this.removeClass("active");
                }
            });
        });

    },
    
    initGymPage: function () {
        ymaps.ready(function() {
            var $map = $("#map"),
                longitude = $map.data("longitude"),
                latitude = $map.data("latitude"),
                coordinates,
                moscowCoordinates = [55.753559, 37.609218];

            if (longitude && latitude) {
                coordinates = [latitude, longitude];
            }
            if (!coordinates) {
                coordinates = moscowCoordinates;
            }
            var map = new ymaps.Map("map", {
                    center: coordinates,
                    zoom: 17,
                    controls: ['typeSelector', 'fullscreenControl', 'zoomControl']
                }),
                placemark = new ymaps.Placemark(
                    coordinates,
                    {},
                    {
                        draggable: false,
                        preset: 'islands#whiteStretchyIcon'
                    }
                );
            map.geoObjects.add(placemark);
        });
    },

    initGymListPage: function () {
        $(".gym-list .sys-change-city").off("click").on("click", function () {
            S.updateGyms($(this).data("cityId"));

            return false;
        });
    },
    
    initLoginPage: function () {
        var form = $("form.login"),
            userNameError = form.find("#login-error");

        var conf = {
            form: ".login",
            scrollToTopOnError: false
        };
        $.validate(conf);

        form.find("button").off("click").on("click", function() {
            if (form.isValid(null, conf, true)) {
                var params = form.serialize();

                Util._postBaseUrl("/account/sign_in", params, function(response) {
                    if (response === "success") {
                        Util.setLocation("/account/");
                    } else {
                        var span = $("<span class='help-block form-error'>Неправильный login или пароль</span>");
                        userNameError.append(span);
                    }
                }, null, null, "text");
            }

            return false;
        });
    },

    initRecoveryPage: function () {
        var emailForm = $("form.email"),
            passwordForm = $("form.password"),
            emailFormConf = {
                form: ".email",
                scrollToTopOnError: false
            },
            passwordFormConf = {
                form: ".password",
                scrollToTopOnError: false
            };

        function initValidation(conf) {
            $.validate(conf);
        }
        initValidation(emailFormConf);
        initValidation(passwordFormConf);
    },

    initAccountPage: function () {
        var athleteForm = $("form.athlete"),
            passwordForm = $("form.password"),
            athleteFormConf = {
                form: ".athlete",
                scrollToTopOnError: false
            },
            passwordFormConf = {
                form: ".password",
                scrollToTopOnError: false
            };

        function initValidation(conf) {
            $.validate(conf);
        }
        initValidation(athleteFormConf);
        initValidation(passwordFormConf);

        athleteForm.find("button").off("click").on("click", function() {
            var params = athleteForm.serialize();

            var valid = athleteForm.isValid(null, athleteFormConf, true);
            valid = S.validateForm(athleteForm, true) && valid;
            if (valid) {
                Util.postOnAjax(params + "&action=save-athlete", function () {
                    initValidation(athleteFormConf);
                    if (S.validateForm(athleteForm, true)) {
                        S.scrollTo(0, 500);
                        S.showAndHide($(".form-success.athlete-success"), 5000);
                    }
                });
            }

            return false;
        });

        passwordForm.find("button").off("click").on("click", function() {
            var params = passwordForm.serialize();

            var valid = passwordForm.isValid(null, passwordFormConf, true);
            valid = S.validateForm(passwordForm, true) && valid;
            if (valid) {
                Util.postOnAjax(params + "&action=save-password", function () {
                    initValidation(passwordFormConf);
                    if (S.validateForm(passwordForm, true)) {
                        S.showAndHide($(".form-success.password-success"), 5000);
                    }
                });
            }

            return false;
        });
    },

};