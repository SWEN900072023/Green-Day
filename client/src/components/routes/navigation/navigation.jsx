import { Fragment } from "react";
import { Outlet, Link } from "react-router-dom";
import "./navigation.style.scss";
import { useSelector } from "react-redux";
import { selectorCurrentUser } from "../../store/user/user.selector";
const Navigation = () => {
  const currentUser = useSelector(selectorCurrentUser);
  return (
    <Fragment>
      <div className="navigation">
        {/* TODO:When user login, we display home icon for user back to home page */}
        <h2>Welcome To Music Events System</h2>
        <div className="nav-links-container">
          {currentUser == null ? (
            <>
              <Link className="nav-link" to={"/"}>
                Sign up
              </Link>
              <Link className="nav-link" to={"/login"}>
                Sign in
              </Link>
            </>
          ) : currentUser.userType === "EventPlanner" ? (
            <>
              <Link className="nav-link" to={"/"}>
                Sign out
              </Link>
              <Link className="nav-link" to={"/eventCreation"}>
                Create Event
              </Link>
            </>
          ) : currentUser.userType === "Customer" ? (
            <>
              <Link className="nav-link" to={"/"}>
                Sign out
              </Link>
              <Link className="nav-link" to={"/mybookings"}>
                Bookings
              </Link>
            </>
          ) : (
            <>
              <Link className="nav-link" to={"/"}>
                Sign out
              </Link>
              <Link className="nav-link" to={"/User"}>
                User Info
              </Link>
              <Link className="nav-link" to={"/venueCreation"}>
                Create Venue
              </Link>
            </>
          )}
        </div>
      </div>
      <Outlet />
    </Fragment>
  );
};

export default Navigation;
