using SnapTown.DAL;
using SnapTown.DTO;
using SnapTown.Models;
using SnapTown.WebService.Converters;
using System.Net.Http;
using System.Web.Http;

namespace SnapTown.WebService.Controllers
{
    [RoutePrefix("api/auth")]
    public class Auth : ApiController
    {
        private IUnitOfWork unitOfWork { get; set; }
        public Auth(IUnitOfWork unitOfWork)
        {
            this.unitOfWork = unitOfWork;
        }

        [Route("")]
        public int Post(RegisterUserDto registerUser)
        {
            //TODO verify facebook

            //register
            var user = UserConverter.AsUser.Compile().Invoke(registerUser);
            this.unitOfWork.Users.Create(user);

            this.unitOfWork.SaveChanges();

            return user.UserID;
        }

        [Route("")]
        public void Put(RegisterUserDto registerUser)
        {
            //TODO verify facebook

            var user = this.unitOfWork.Users.Get(u => u.UserID == registerUser.UserID &&
                 u.AuthToken == registerUser.OldAuthToken);

            user.AuthToken = registerUser.AuthToken;
            this.unitOfWork.Users.Update(user);

            this.unitOfWork.SaveChanges();
        }
    }
}
