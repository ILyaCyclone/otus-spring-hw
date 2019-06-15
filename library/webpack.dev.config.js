const HtmlWebpackPlugin = require('html-webpack-plugin')
const path = require('path');

module.exports = {
    entry: './src/ui/index.js',
    devtool: 'inline-source-map',
    output: {
        path: path.resolve(__dirname),
        filename: 'bundle.js',
        libraryTarget: 'umd'
    },
    /*
        devServer: {
            contentBase: path.resolve(__dirname) + '/src/ui',
            compress: true,
            port: 9000,
            host: 'localhost',
            open: true,
            before: (app) => {
                app.get('/api/persons', (req, res) => res.send([
                    {id: '1', name: 'Привяу'}
                ]));
            }
        },
        */
    devServer: {
        port: '8081',
        proxy: {
            '/api': {
                target: 'http://localhost:8080',
                changeOrigin: true,
                secure: false
            },
        },
        after(app) {
            app.get('*', (req, res) => {
                // supports routes
                req.url = '/index.html';
                // noinspection JSAccessibilityCheck
                app.handle(req, res);
            });
        }
    },


    module: {
        loaders: [
            {
                test: /\.js$/,
                exclude: /(node_modules|bower_components|build)/,
                use: {
                    loader: 'babel-loader',
                    options: {
                        presets: ['env', 'react']
                    }
                }
            }
        ]
    },
    plugins: [
        new HtmlWebpackPlugin({
            filename: 'index.html',
            template: 'src/ui/index.html'
        })
    ]
}