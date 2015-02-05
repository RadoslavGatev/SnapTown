using SnapTown.DAL;
using SnapTown.DTO;
using SnapTown.Models;
using SnapTown.WebService.Converters;
using System;
using System.Linq;
using System.Linq.Expressions;
using System.Net;
using System.Web.Http;

namespace SnapTown.WebService.Controllers
{
    [RoutePrefix("api/towns")]
    public class TownsController : ApiController
    {
        private IUnitOfWork unitOfWork { get; set; }
        public TownsController(IUnitOfWork unitOfWork)
        {
            this.unitOfWork = unitOfWork;
        }

        /// <summary>
        ///  Get all towns for which the user have subscription
        /// </summary>
        /// <param name="authToken"> The authentication token of the user </param>
        public IQueryable<TownDto> Get(string authToken)
        {
            var user = unitOfWork.Users.Get(u => u.AuthToken == authToken);
            if (user == null)
            {
                throw new HttpResponseException(HttpStatusCode.NotFound);
            }

            return this.unitOfWork.Subscriptions.Filter(s => s.UserID == user.UserID, new string[] { "Town" })
                .Select(x => x.Town).Select(TownConverter.AsSubscribedTownDto);
        }

        [Route("{townId:int}")]
        public TownDto GetById(int townId)
        {
            //TODO is authenticated
            var town = this.unitOfWork.Towns
                   .Get(t => t.TownID == townId, new string[] { "Country" });

            if (town == null)
            {
                throw new HttpResponseException(HttpStatusCode.NotFound);
            }

            return TownConverter.AsTownDto.Compile().Invoke(town);
        }

        [Route("{townId:int}/subscribe")]
        [HttpPost]
        public void Subscribe(int townId, string authToken)
        {
            var user = this.unitOfWork.Users.Get(u => u.AuthToken == authToken);
            var doesSubscriptionExist = this.unitOfWork.Subscriptions.Contains(s => s.TownID == townId &&
            s.User.UserID == user.UserID);

            if (!doesSubscriptionExist)
            {
                var subscription = new Subscription()
                {
                    TownID = townId,
                    User = user
                };

                this.unitOfWork.Subscriptions.Create(subscription);
                this.unitOfWork.SaveChanges();
            }
        }

        [Route("{townId:int}/subscribe")]
        [HttpDelete]
        public void Unsubscribe(int townId, string authToken)
        {
            var user = this.unitOfWork.Users.Get(u => u.AuthToken == authToken);
            var subscription = this.unitOfWork.Subscriptions.Get(s => s.TownID == townId &&
            s.User.UserID == user.UserID);

            if (subscription != null)
            {
                this.unitOfWork.Subscriptions.Delete(subscription);
                this.unitOfWork.SaveChanges();
            }
        }
    }
}
