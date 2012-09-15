require.config({
    baseUrl: '../js',

    shim: {
        'jquery': {
            exports: '$'
        },
        'lodash': {
           exports: '_'
        },
        'modernizr': {
            exports: 'Modernizr'
        },
    },

    paths: {
        jquery: 'jquery',
        lodash: 'lodash',
        modernizr: 'modernizr',
    }
});