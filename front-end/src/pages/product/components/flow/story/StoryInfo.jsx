import { ArrowLeft } from "lucide-react";
import RefContents from "./RefContents";
import StoryDetail from "./StoryDetail";
import { useNavigate, useOutletContext } from "react-router-dom";
import { useEffect } from "react";

export default function StoryInfo() {
  const navigate = useNavigate();

  const { setIsHeaderVisible } = useOutletContext();

  useEffect(() => {
    setIsHeaderVisible(false);

    return () => setIsHeaderVisible(true);
  }, []);

  return (
    <div className="flex flex-col items-center justify-between w-full h-full border rounded shadow-md">
      <div className="w-full ">
        <ArrowLeft className="m-2 mt-3 ml-3 " onClick={() => navigate(-1)} />
      </div>
      <div className="flex items-center justify-center w-full h-full p-4 ">
        <StoryDetail />
        <RefContents />
      </div>
    </div>
  );
}
