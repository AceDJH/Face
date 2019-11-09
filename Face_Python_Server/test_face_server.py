# coding=utf-8

import sys
import json
import base64

from urllib.request import urlopen
from urllib.request import Request
from urllib.error import URLError
from urllib.parse import urlencode
from urllib.parse import quote_plus

import os
from flask import Flask, jsonify
from flask import request
app = Flask(__name__)

# 防止https证书校验不正确
import ssl
ssl._create_default_https_context = ssl._create_unverified_context

API_KEY = '9M0xo4ox8GwhrwX53z7rdsCW'

SECRET_KEY = 'GXmXxrqOHqn9QxhxCohVv1ezVRHlXuWq'


IMAGE_RECOGNIZE_URL = "https://aip.baidubce.com/rest/2.0/image-classify/v2/advanced_general"


"""  TOKEN start """
TOKEN_URL = 'https://aip.baidubce.com/oauth/2.0/token'

basedir = os.path.abspath(os.path.dirname(__file__))  # 定义一个根目录 用于保存图片用


@app.route("/upload", methods=['POST', 'GET'])
def upload():
    # 获取图片文件 name = upload
    img = request.files.get('image')

    # 在windows上的测试代码
    # 定义一个图片存放的位置 存放在static下面
    # 在linux服务器上的代码
    # path = basedir + "/static/img/"
    # 在windows上的测试代码
    path = basedir

    # 图片名称
    imgName = img.filename

    # 图片path和名称组成图片的保存路径
    file_path = path + '\\' + imgName

    print('path_____' + file_path)

    # 保存图片
    img.save(file_path)

    # 获取access token
    token = fetch_token()

    # 拼接图像识别url
    url = IMAGE_RECOGNIZE_URL + "?access_token=" + token

    file_content = read_file(file_path)

    # 请求百度大脑得到结果
    response = requestData(url, urlencode(
        {
            'image': base64.b64encode(file_content),
            'baike_num': 3
        }))
    result_json = json.loads(response)
    # 打印图片结果, 只要前3个结果
    resultBean = result_json["resultBean"]
    resultBean = resultBean[:3]
    print(resultBean)
    # 真正的结束地方
    return jsonify(result_json)

    # a = None
    # for data in resultBean[:3]:
    #     print(u"  名称：  " + data["keyword"])
    #     a = data["keyword"]
    # return a


@app.route("/", methods=['GET'])
def get_index():
    return 'Index Page'


@app.route("/test", methods=['GET'])
def get_photo():
    return 'test'


"""
    获取token
"""


def fetch_token():
    params = {'grant_type': 'client_credentials',
              'client_id': API_KEY,
              'client_secret': SECRET_KEY}
    post_data = urlencode(params)
    post_data = post_data.encode('utf-8')
    req = Request(TOKEN_URL, post_data)
    try:
        f = urlopen(req, timeout=5)
        result_str = f.read()
    except URLError as err:
        print(err)
    result_str = result_str.decode()

    resultBean = json.loads(result_str)

    if ('access_token' in resultBean.keys() and 'scope' in resultBean.keys()):
        if not 'brain_all_scope' in resultBean['scope'].split(' '):
            print('please ensure has check the  ability')
            exit()
        return resultBean['access_token']
    else:
        print('please overwrite the correct API_KEY and SECRET_KEY')
        exit()


"""
    读取文件
"""


def read_file(image_path):
    f = None
    try:
        f = open(image_path, 'rb')
        return f.read()
    except:
        print('read image file fail')
        return None
    finally:
        if f:
            f.close()


"""
    调用远程服务
"""


def requestData(url, data):
    req = Request(url, data.encode('utf-8'))
    has_error = False
    try:
        f = urlopen(req)
        result_str = f.read()
        result_str = result_str.decode()
        return result_str
    except URLError as err:
        print(err)


"""
    调用通用物体识别接口并打印结果
    接收x个图0-5
"""


def print_result(filename, url):
    # 获取图片
    file_content = read_file(filename)

    response = requestData(url, urlencode(
        {
            'image': base64.b64encode(file_content),
            'baike_num': 3
        }))
    result_json = json.loads(response)

    # 打印图片结果, 只要前3个结果
    resultBean = result_json["resultBean"]
    for data in resultBean[:3]:
        print(u"  名称：  " + data["keyword"])
        print(u"  分类：  " + data["root"])
        print(u"  置信度:  " + str(data["score"]))
        baike = data["baike_infoBean"]
        if len(baike) != 0:
            print(u"  百科内容描述：  " + baike["description"])


if __name__ == "__main__":
    app.run()
