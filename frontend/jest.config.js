const path = require('path');

module.exports = {
    transform: {
        '^.+\\.js$': 'babel-jest',
        '^.+\\.vue$': 'vue-jest'
    },
    moduleFileExtensions: ['js', 'vue'],
    testEnvironment: 'jsdom',
    transformIgnorePatterns: [
        '/node_modules/(?!axios/)'  // Excepción para axios si es necesario
    ],
    moduleNameMapper: {
        '^@/(.*)$': '<rootDir>/src/$1',  // Este es el mapeo de alias para que Jest entienda el alias `@`
    }
};
