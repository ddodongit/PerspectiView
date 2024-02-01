import logoImg from "@/assets/logo.svg"

export default function CharInfo () {
  
  return (
    <div className="flex flex-col">
      <img src={logoImg} alt="mock image" width={64} height={64} />
      <div className="box-border flex justify-center m-1"> 
        책 
        {/* 해당 이미지와 텍스트는 mock */}
      </div>
    </div>
  );

}
