import { Fragment } from "react";
import { Outlet, Link } from "react-router-dom";
import "./navigation.style.scss";
const Navigation = () => {
  return (
    <Fragment>
      <div className="navigation">
        <h2>Welcome To Music Events System</h2>
        <div className="nav-links-container">
          <Link className="nav-link" to={"/"}>
            Sign up
          </Link>
          <Link className="nav-link" to={"/login"}>
            Sign in
          </Link>
        </div>
      </div>
      <Outlet />
    </Fragment>
  );
};

export default Navigation;
