/*
 * @Author: lx.jin 308561217@qq.com
 * @Date: 2023-03-24 23:22:53
 * @LastEditors: lx.jin 308561217@qq.com
 * @LastEditTime: 2023-03-28 19:31:28
 * @FilePath: /cloud-cap-samples-java-main/app/login/app.js
 * @Description: 这是默认设置,请设置`customMade`, 打开koroFileHeader查看配置 进行设置: https://github.com/OBKoro1/koro1FileHeader/wiki/%E9%85%8D%E7%BD%AE
 */

const httpClient = axios.create({
  withCredentials: true,
});

// adding csrf token to request headers
// axios
//   .head("/api/browse", {
//     headers: {
//       "X-CSRF-Token": "Fetch",
//       "X-Requested-With": "XMLHttpRequest",
//     },
//   })
//   .then((res) => {
//     xcsrfToken = res.headers["x-csrf-token"];
//     httpClient.defaults.headers.common["X-CSRF-Token"] = xcsrfToken;
//   });

const GET = (url) => httpClient.get("/api/browse" + url);
const bfLogin = (data) => httpClient.post("/bfLogin", data);

const Login = () => {
  const [number, setNumber] = React.useState(0);

  const onFinish = async (values) => {
    const result = await bfLogin(values)
    console.log('Success:', values, result);
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
          initialValues={{ remember: true }}
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