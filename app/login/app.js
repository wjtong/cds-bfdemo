/*
 * @Author: lx.jin 308561217@qq.com
 * @Date: 2023-03-24 23:22:53
 * @LastEditors: lx.jin 308561217@qq.com
 * @LastEditTime: 2023-03-28 20:22:26
 * @FilePath: /cloud-cap-samples-java-main/app/login/app.js
 * @Description: 这是默认设置,请设置`customMade`, 打开koroFileHeader查看配置 进行设置: https://github.com/OBKoro1/koro1FileHeader/wiki/%E9%85%8D%E7%BD%AE
 */

const httpClient = axios.create({
  withCredentials: true,
});

const bfLogin = async (data) => httpClient.post("/bfLogin", data).then((res) => {
  const { data, code } = res.data
  if (code < 300) {
    const xcsrfToken = res.headers["x-csrf-token"];
    console.log({ xcsrfToken })
    httpClient.defaults.headers.common["X-CSRF-Token"] = xcsrfToken;
    return data
  } else {
    antd.message.error(data.data);
    return false
  }
});

const Login = () => {
  const onFinish = async (values) => {
    const result = await bfLogin(values)
    console.log('Success:', { values, result, window: window.location });
    if (result) {
      const href = window.location.origin + '/custrequestmanage/webapp/index.html#Shell-home'
      console.log({ href })
      location.replace(href);
    }
  };

  const onFinishFailed = (errorInfo) => {
    console.log('Failed:', errorInfo);
  };

  return (
    <div className='container'>
      <antd.Card
        title="用户登录"
        style={{
          width: 500,
        }}
      >
        <antd.Form
          layout='vertical'
          name="basic"
          labelCol={{ span: 8 }}
          wrapperCol={{ span: 24 }}
          initialValues={{ userLoginId: 'bfadmin', password: 'cap' }}
          onFinish={onFinish}
          onFinishFailed={onFinishFailed}
          autoComplete="off"
        >
          <antd.Form.Item
            label="用户账号："
            name="userLoginId"
            rules={[{ required: true, message: '用户账号不能为空!' }]}
          >
            <antd.Input />
          </antd.Form.Item>

          <antd.Form.Item
            label="登录密码："
            name="password"
            rules={[{ required: true, message: '登录密码不能为空!' }]}
          >
            <antd.Input.Password />
          </antd.Form.Item>

          <antd.Form.Item>
            <antd.Button type="primary" htmlType="submit" block>
              登录
            </antd.Button>
          </antd.Form.Item>
        </antd.Form>
      </antd.Card>
    </div>

  )
}

ReactDOM.render(
  React.createElement(Login),
  document.getElementById('root')
);