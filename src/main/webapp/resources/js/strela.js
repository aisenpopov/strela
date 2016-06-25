var S = {

    initBase: function() {
    },
    
    initGymPage: function () {
        ymaps.ready(function() {
            var $map = $("#map"),
                longitude = $map.data("longitude"),
                latitude = $map.data("latitude"),
                coordinates,
                moscowCoordinates = [55.753559, 37.609218];

            if (longitude.length && latitude.length) {
                coordinates = [parseFloat(latitude), parseFloat(longitude)];
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
    }
    
};