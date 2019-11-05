import os
import base64
from flask import Flask
from flask import request
app = Flask(__name__)

basedir = os.path.abspath(os.path.dirname(__file__))  # 定义一个根目录 用于保存图片用


@app.route("/upload", methods=['POST', 'GET'])
def upload():
    # 获取图片文件 name = upload
    img = request.files.get('image')

    # 定义一个图片存放的位置 存放在static下面
    # 在linux服务器上的代码
    # path = basedir + "/static/img/"
    # 在windows上的测试代码
    path = basedir

    print('path_____' + path)

    # 图片名称
    imgName = img.filename

    # 图片path和名称组成图片的保存路径
    file_path = path + imgName

    # 保存图片
    img.save(file_path)

    # url是图片的路径
    url = '/static/img/' + imgName
    return url


@app.route("/", methods=['GET'])
def get_index():
    return 'Index Page'


@app.route("/test", methods=['GET'])
def get_photo():
    return 'test'


if __name__ == "__main__":
    app.run()
