import { useState } from "react";

export default function ReadMore({ children }) {
  const text = children;
  const [isReadMore, setIsReadMore] = useState(true);
  const toggleReadMore = () => {
    setIsReadMore(!isReadMore);
  };
  return (
    <div>
      {isReadMore ? text?.slice(0, 90) : text}

      <span
        onClick={toggleReadMore}
        className={`pl-1 font-bold read-or-hide text-primary-accent ${
          text?.length > 90 ? "" : "hidden"
        }`}
      >
        {isReadMore ? "...더보기" : " 접기"}
      </span>
    </div>
  );
}
